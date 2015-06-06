#include <string>
#include <cstdio>
#include <algorithm>
#include <vector>
#include <map>
#include <set>

#include "../../OleanderStemmingLibrary/stemming/english_stem.h"


using namespace std;

const double PR_K = 0.003;
const double TF_IDF_k = 1000;

string text_dir = "";
string index_file = "";


void to_low(string &word) 
{
    for (int i = 0; i < (int)word.size(); i++) 
    {
        if (word[i] >= 'A' && word[i] <= 'Z') 
        {
            word[i] = word[i] - 'A' + 'a';
        }

    }
}



class R_file 
{

public:

    int id;
    double pageRank, tf, idf, tf_idf, bm25;

    R_file() {}

    R_file(int id, double pageRank, double tf, double idf, double tf_idf, double bm25):
            id(id), pageRank(pageRank), tf(tf), idf(idf), tf_idf(tf_idf), bm25(bm25) {}

    bool operator<(const R_file &second) 
    {
        double myWeight = pageRank * PR_K + tf_idf * TF_IDF_k;
        double secondWeight = second.pageRank * PR_K + second.tf_idf * TF_IDF_k;

        return myWeight < secondWeight;
    }
};


class article 
{
    public:
        long int from, to, word_count;
        string url;
        double pageRank;

    article(long int from, long int to, long int word_count, string url,
                double pageRank = 10): from(from), to(to), word_count(word_count), url(url), pageRank(pageRank) {}

};


map<pair<long long, long long>, vector<R_file> > indexes;
vector<article> info;


void read_info() 
{
    FILE *id = fopen((text_dir + "id.dat").c_str(), "r");
    long int from, to, word_count;
    double pageRank;
    char buff[200];

    while (fscanf(id, "%ld %ld %ld %lf %s", &from, &to, &word_count,  &pageRank, buff) != EOF) 
    {
        info.push_back(article(from, to, word_count,  buff, pageRank));
    }

    fclose(id);
}


void add_in_map(long long hash1, long long hash2, int id, double pageRank,
                            double tf, double idf, double tf_idf, double bm25) 
{
    pair<long long, long long> hash (hash1, hash2);
    map<pair<long long, long long>, vector<R_file> > :: iterator it = indexes.find(hash);

    if (it == indexes.end()) 
    {
        indexes[hash] = vector<R_file> (0);
        it = indexes.find(hash);
    }

    it -> second.push_back(R_file(id, pageRank, tf, idf, tf_idf, bm25));
}


void read_index() 
{
    FILE *in = fopen(index_file.c_str(), "r");
    long long hash1, hash2;
    int id;
    double pageRank, tf, idf, tf_idf, bm25;
    int n;

    while (fscanf(in, "%lld %lld %d", &hash1, &hash2, &n) != EOF)
    {
        for (int i = 0; i < n; i++) 
        {
             fscanf(in, "%d %lf %lf %lf %lf %lf\n", 
                    &id, 
                    &pageRank, 
                    &tf, 
                    &idf, 
                    &tf_idf, 
                    &bm25 ); 
            add_in_map(hash1, hash2, id, pageRank, tf, idf, tf_idf, bm25);
        }
    }


    fclose(in);
}


string steamm(string ANSIWord) 
{
    stemming::english_stem<> StemEnglish;
    wchar_t* UnicodeTextBuffer = new wchar_t[ANSIWord.length()+1];
    wmemset(UnicodeTextBuffer, 0, ANSIWord.length()+1);
    mbstowcs(UnicodeTextBuffer, ANSIWord.c_str(), ANSIWord.length());
    wstring word = UnicodeTextBuffer;
    StemEnglish(word);

    return string(word.begin(), word.end());
}

const long long MOD1 = 1073676287LL;
const long long MOD2 = 2971215073LL;
const long long base = 241;

pair<long long, long long> getHash(string s) 
{
    while (s.size() > 0 && s[(int)s.size() - 1] == '\n') 
    {
        s = s.substr(0, (int)s.size() - 1);
    }

    long long hash1 = 0;
    long long hash2 = 0;
    long long power1 = 1;
    long long power2 = 1;

    for (int i = 0; i < (int)s.size(); i++) 
    {
        (hash1 += s[i] * power1) %= MOD1;
        (hash2 += s[i] * power2) %= MOD2;
        (power1 *= base) %= MOD1;
        (power2 *= base) %= MOD2;
    }

    hash1 += 100LL * MOD1;
    hash1 %= MOD1;
    hash2 += 100LL * MOD2;
    hash2 %= MOD2;

    return make_pair(hash1, hash2);
}

class page 
{

public:
    int id;
    double rating;

    page(int id, double rating): id(id), rating(rating) {}

    bool operator<(const page &second) 
    {
        return rating > second.rating;
    }

};


vector<double> rating;


void get_id(string w) 
{
   // for (int i = 1; i <= (int)w.size(); i++) 
   // {
//        string sub = w.substr(0, i); 
        pair<long long, long long> hash = getHash(w);
        map<pair<long long, long long>, vector<R_file> > :: iterator it = indexes.find(hash);

        if (it == indexes.end()) 
        {
            return;
        }

        for (int i = 0; i < (int)it -> second.size(); i++) 
        {
            rating[it -> second[i].id] += it -> second[i].pageRank * PR_K + it -> second[i].tf_idf * TF_IDF_k;         
        }
    //}

}



vector<int> get_out() 
{

    vector<page> result;

    for (int i = 0; i < (int)rating.size(); i++) 
    {
        if (rating[i] > 0) 
        {
            result.push_back(page(i, rating[i]));
        }
    }

    sort(result.begin(), result.end());

    vector<int> to_return;
    for (int i = 0; i < (int)result.size() && i < 50; i++) 
    {
        to_return.push_back(result[i].id);
    }

    return to_return;
}


void print_article_with_id(int id) 
{
    FILE *in = fopen((text_dir + "articles.txt").c_str(), "r");

    fseek(in, info[id].from, SEEK_SET);

    char buff[20000];

    while (ftell(in) < info[id].to && fgets(buff, 20000, in) != NULL)
    {
        puts(buff);
    }

    fclose(in);
}

vector<string> words;

void add(string temp) 
{
    words.push_back(steamm(temp));
    to_low(words[(int)words.size() - 1]);
    get_id(words[(int)words.size() - 1]);
}


void init() 
{
    words.clear();
    rating= vector<double> (info.size(), 0);
}



void endless_loop() 
{
    while (42) {
        init();

        printf("Print your request:\n");
        char buff[10000];
        gets(buff);
        if ((string)buff == "exit") 
        {
            break;
        }

        int len = strlen(buff);
        string temp = "";

        for (int i = 0; i < len; i++) 
        {
            if (buff[i] == ' ' || buff[i] == '\n' || buff[i] == '.' || buff[i] == ',') 
            {
                if (temp.size() > 0) 
                {
                    add(temp);
                }

                temp = "";
            }

            temp += buff[i];
        }
        if (temp.size() > 0) 
        {
            add(temp);
        }

        if (words.size() == 0) 
        {
            printf("Try again please!\n");
            continue;
        }



        vector<int> maybe_id = get_out(); 

        for (int i = 0; i < (int)maybe_id.size(); i++) 
        {
           printf("%d. %s\n", i, info[maybe_id[i]].url.c_str());
        }
        
        puts("Choose article: ");
        gets(buff);
        int id;
        sscanf(buff, "%d", &id);

        print_article_with_id(maybe_id[id]);
    }
}




int main(int argc, char *argv[]) 
{
    text_dir = "../../text_files/";
    index_file = "../../index.dat";
    
    read_info();
    read_index();

    if (argc >= 2) 
    { 
        vector<string> words;

        for (int i = 1; i < argc; i++) 
        {
            words.push_back(steamm(argv[i]));
            to_low(words[i - 1]);
            get_id(words[i - 1]);
        }

        if (words.size() == 0) 
        {
            printf("Try again please!\n");
            return 0;
        }

       vector<int> maybe_id = get_out(); 

       for (int i = 0; i < (int)maybe_id.size(); i++) 
       {
           printf("%d. %s\n", i, info[maybe_id[i]].url.c_str());
       }

       char buff[10];
       gets(buff);
       int id;
       sscanf(buff, "%d", &id);

       print_article_with_id(maybe_id[id]);
    }


    endless_loop();


    return 0;
}

