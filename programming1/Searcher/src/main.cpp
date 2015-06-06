#include <string>
#include <cstdio>
#include <algorithm>
#include <vector>
#include <map>
#include <set>

#include "../../OleanderStemmingLibrary/stemming/english_stem.h"
#include "../../hashlib/hash.h"


using namespace std;

const double PR_K = 0.5;
const double TF_IDF_K = 0.5;

string text_dir = "";
string index_file = "";

double min_pR = 0.15;
double max_pR = 0.15;
double min_tf_idf = 5000;
double max_tf_idf = -5000;


double squeze_pageRank(double pR) 
{
    return (10.0 / (max_pR - min_pR)) * (pR - min_pR) + 0; // Convert pR form 0 to 10
}


double squeze_tf_idf(double tf_idf) 
{
    return (10.0 / (max_tf_idf - min_tf_idf)) * (tf_idf - min_tf_idf) + 0;
}

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
        max_pR = max(max_pR, pageRank);
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

    while (fscanf(in, "%lld %lld %d", &hash1, &hash2, &n) != EOF) // hash1 hash2 {number of indexes}
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
            // format: id pageRank tf idf tf-idf bm25
            add_in_map(hash1, hash2, id, pageRank, tf, idf, tf_idf, bm25);
            min_tf_idf = min(min_tf_idf, tf_idf);
            max_tf_idf = max(max_tf_idf, tf_idf);
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
    pair<long long, long long> hash = getHash(w);
    map<pair<long long, long long>, vector<R_file> > :: iterator it = indexes.find(hash);

    if (it == indexes.end()) 
    {
        return;
    }

    for (int i = 0; i < (int)it -> second.size(); i++) 
    {
        rating[it -> second[i].id] += squeze_pageRank(it -> second[i].pageRank) * PR_K + squeze_tf_idf(it -> second[i].tf_idf) * TF_IDF_K;         
    }
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


void get_words_from_string(string request) 
{
    string temp = "";

    for (int i = 0; i < (int)request.size(); i++) 
    {
        if (request[i] == ' ' || request[i] == '\n' || request[i] == '.' || request[i] == ','
                || request[i] == ':' || request[i] == '-') 
        {
            if (temp.size() > 0) 
            {
                add(temp);
            }

            temp = "";
            continue;
        }

        temp += request[i];
    }

    if (temp.size() > 0) 
    {
        add(temp);
    }
}

void NoArticlesException() 
{
    printf("Try something else please!\n");
}


void print_top_n(int n) 
{
    vector<page> pages;

    for (int i = 0; i < (int)info.size(); i++) 
    {
        pages.push_back(page(i, info[i].pageRank));
    }

    sort(pages.begin(), pages.end());

    for (int i = 0; i < n; i++) 
    {
        printf("%d. %s , with rating = %lf\n", i, info[pages[i].id].url.c_str(), squeze_pageRank(pages[i].rating));
    }
}


bool user_wanted_to_see_top(string s) 
{
    if (s.substr(0, 4) == "/top") 
    {
        int n;
        sscanf(s.c_str(), "/top %d", &n);
        print_top_n(n);
        return true;
    }
    return false;
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
        if (user_wanted_to_see_top(buff)) 
        {
            continue;
        }

        get_words_from_string(buff);

        if (words.size() == 0) 
        {
            NoArticlesException();
            continue;
        }

        vector<int> maybe_id = get_out(); 
        if (maybe_id.size() == 0) 
        {
            NoArticlesException();
            continue;
        }

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


int main() 
{
    text_dir = "../../text_files/";
    index_file = "../../index.dat";

    printf("Loading, please, wait\n");    

    read_info();
    read_index(); // 10-15 seconds .....

    endless_loop();

    return 0;
}

