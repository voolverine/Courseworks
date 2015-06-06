#include <cstdio>
#include <vector>
#include <algorithm>
#include <map>

#include "counter.h"

using namespace std;

string text_dir = "";
string index_file = "";

class R_file 
{

public:

    int id;
    double pageRank, tf, idf, tf_idf, bm25;

    R_file(int id, double pageRank): id(id), pageRank(pageRank) {
        tf = 0;
        idf = 0;
        tf_idf = 0;
        bm25 = 0;
    }

    R_file() {}

    R_file(int id, double pageRank, double tf, double idf, double tf_idf, double bm25):
            id(id), pageRank(pageRank), tf(tf), idf(idf), tf_idf(tf_idf), bm25(bm25) {}


    
    void set_tf(double new_tf) 
    {
        tf = new_tf;
    }

    void set_idf(double new_idf) 
    {
        idf = new_idf;
    }

    void set_tf_idf() 
    {
        tf_idf = tf * idf;
    }

    void set_bm25(double new_bm25) 
    {
        bm25 = new_bm25;
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

map<pair<long long, long long>, vector<R_file> > index;
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


void add_words_in_index() 
{
    FILE *articles = fopen((text_dir + "articles.txt").c_str(), "r");

    for (int i = 0; i < /*(int)info.size()*/1000; i++) 
    {
        fseek(articles, info[i].from, SEEK_SET);

        char buff[20000];
        string current_article = "";
        while (fgets(buff, 20000, articles) && ftell(articles) < info[i].to && !feof(articles)) 
        {
            current_article += buff; 
        }
        
        vector<string> words = get_dif_words_in_article(current_article);

        for (int j = 0; j < (int)words.size(); j++) 
        {
            pair<long long, long long> hash = getHash(words[j]);

            map<pair<long long, long long>, vector<R_file> > :: iterator it = index.find(hash);

            if (it == index.end()) 
            {
                index[hash] = vector<R_file> (0);
                it = index.find(hash);
            }
        
            it -> second.push_back(R_file(i, info[i].pageRank)); 
            int pos = it -> second.size() - 1;

            it -> second[pos].set_tf((double)get_count_of_word(words[j], current_article) / (double)info[i].word_count);
        }

        if (i % 100 == 0) 
        {
            printf("%lf percents of indexing complited\n", (double)i * 100 / (double)info.size());
        }
    }
}


void save_index() 
{
    FILE *out = fopen(index_file.c_str(), "w");
    
    for (map<pair<long long, long long>, vector<R_file> > :: iterator it = index.begin(); it != index.end();
            it++) 
    {
        fprintf(out, "%lld %lld %d\n", it -> first.first, it -> first.second, (int)it -> second.size());
        for (int i = 0; i < (int)it -> second.size(); i++) 
        {
            fprintf(out, "%d %lf %lf %lf %lf %lf\n", 
                    it -> second[i].id,
                    it -> second[i].pageRank,
                    it -> second[i].tf,
                    it -> second[i].idf,
                    it -> second[i].tf_idf,
                    it -> second[i].bm25);
        }
    }
    
    fclose(out);
}


void add_in_map(long long hash1, long long hash2, int id, double pageRank,
                            double tf, double idf, double tf_idf, double bm25) 
{
    pair<long long, long long> hash (hash1, hash2);
    map<pair<long long, long long>, vector<R_file> > :: iterator it = index.find(hash);

    if (it == index.end()) 
    {
        index[hash] = vector<R_file> (0);
        it = index.find(hash);
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
            printf("%lld %lld\n", hash1, hash2);
        }
    }


    fclose(in);
}


void calc_idf() 
{
    for (map<pair<long long, long long>, vector<R_file> > :: iterator it = index.begin();
                it != index.end(); it++) 
    {
        double idf = log((double)info.size() / (double)it -> second.size());

        for (int i = 0; i < (int)it -> second.size(); i++) 
        {
            it -> second[i].set_idf(max(idf, 1e-2));
            it -> second[i].set_tf_idf();
        }
    }
}


int main() 
{
    text_dir = "../../text_files/";
    index_file = "../../index.dat";
    
    read_info();
    add_words_in_index();
    calc_idf();
    save_index();
//    read_index();

    return 0;
}

