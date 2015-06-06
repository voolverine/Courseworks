#include <cstdio>
#include <cstdio>
#include <string>
#include <string.h>
#include <vector>
#include <algorithm>
#include <stack>
#include <map>
#include <thread>
#include <queue>
#include <ctime>

#include "text_parser.h"

#define EPS 3e-1

using namespace std;

string html_dir;
string text_dir;

bool finished = false;

class edge 
{

public:

    int to;
    double flow;

    edge(int to, double flow): to(to), flow(flow) {}

    void change_flow(double new_flow) 
    {
        flow = new_flow;
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


map<pair<long long, long long>, int> saved; // url_hash -> id
vector<article> info; // id -> article info
vector<vector<edge> > graph; // graph from id

const long long MOD1 = 1073676287LL;
const long long MOD2 = 2971215073LL;
const long long base = 241;


FILE *articles = NULL;


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

    return make_pair(hash1, hash2);
}


int get_id(string s) 
{
    pair<long long, long long> hash = getHash(s);
    map<pair<long long, long long>, int>::iterator it = saved.find(hash);
    if (it != saved.end()) 
    {
        return it -> second;
    } else 
    {
        return -1;
    }
}


vector<string> get_filenames_in_dir(string dir) 
{
    vector<string> result;

    FILE *names;
    names = popen(("ls -a1 " + dir).c_str(), "r");
    
    char buff[1000];
    while (!feof(names) && fgets(buff, 1000, names)) 
    {
        string new_file = buff;
        while (new_file.size() > 0 && new_file[(int)new_file.size() - 1] == '\n') 
        {
            new_file = new_file.substr(0, (int)new_file.size() - 1);
        }

        result.push_back(new_file);
    }

    return result;
}



bool already_saved(string url) 
{
    pair<long long, long long> hash = getHash(url);

    if (saved.find(hash) == saved.end()) 
    {
        return false;
    }
    return true;
}


int get_word_count(pair<string, string> text) 
{
    int word_count = 0;
    for (int i = 0; i < (int)text.first.size(); i++) 
    {
        if (text.first[i] == ' ') 
        {
            word_count++;
        }

    }

    for (int i = 0; i < (int)text.second.size(); i++) 
    {
        if (text.second[i] == ' ') 
        {
            word_count++;
        }
    }
    
    return word_count + 2;
}



void save_text(string &title, string &text, string url) 
{
    fseek(articles, 0, SEEK_END); 

    long int from = ftell(articles);
    saved[getHash(url)] = (int)info.size() - 1;

    fprintf(articles, "%s\n\n", title.c_str());
    fprintf(articles, "%s\n", text.c_str()); 

    long int to = ftell(articles);
    info.push_back(article(from, to, get_word_count(make_pair(title, text)), url));    
    return;
}


void Parse_all_files() 
{
    vector<string> all_files = get_filenames_in_dir(html_dir);

    for (int i = 0; i < (int)all_files.size(); i++) 
    {   
        if (all_files[i] == "." || all_files[i] == "..") 
        {
            continue;
        }
        
        pair<string, string> text = parse(html_dir + all_files[i]);

        if (!already_saved(get_url_from_title(text.first))) 
        {
            save_text(text.first, text.second, get_url_from_title(all_files[i]));
        }

        if (finished) 
        {
            break;
        }
        if (i % 100 == 0) {
            printf("%lf percents of parsing complited.\n", (double) i * 100.0 / (double)all_files.size());
        }
    }
    
    printf("Parsing finished. Press Enter.\n");
    return;
}


void add_to_saved(int id, string url) 
{
    saved[getHash(url)] = id;
}


void read_info() 
{
    FILE *id = fopen((text_dir + "id.dat").c_str(), "r");
    long int from, to, word_count;
    double pageRank;
    char buff[200];

    while (fscanf(id, "%ld %ld %ld %lf %s", &from, &to, &word_count,  &pageRank, buff) != EOF) 
    {
        if (!already_saved(buff)) 
        {
            info.push_back(article(from, to, word_count,  buff, pageRank));
            add_to_saved((int)info.size() - 1, buff);
        }
    }

    fclose(id);
}


void save_info() 
{
    FILE *id = fopen((text_dir + "id.dat").c_str(), "w");
    
    for (int i = 0; i < (int)info.size(); i++) 
    {
        fprintf(id, "%ld %ld %ld %lf %s\n", info[i].from, info[i].to, info[i].word_count, info[i].pageRank, info[i].url.c_str());  
         // Format: |from -> to|, word_count, pageRank, url
    }

    fclose(id);
}


void remove_all_copies(string dir) 
{
    vector<string> all_files = get_filenames_in_dir(dir);

    for (int i = 0; i < (int)all_files.size(); i++) 
    {
        pair<string, string> file = parse(html_dir + all_files[i]);
        file.first = get_url_from_title(file.first);
        system(("mv \"" + html_dir + all_files[i] + "\" \"" + html_dir + file.first.c_str() + "\"").c_str());
    }

}


void make_graph() 
{
    graph.resize(info.size());

    for (int i = 0; i < (int)info.size(); i++) 
    {
        int current_id = get_id(info[i].url);
        if (current_id != -1) {
            vector<string> all_urls = get_all_urls("../../html_files/" + info[i].url);

            for (int i = 0; i < (int)all_urls.size(); i++) 
            {
                int to_id = get_id(all_urls[i]); 
                if (to_id != -1 && to_id != current_id) 
                {
                    graph[current_id].push_back(edge(to_id, 0));
                }
            }

            graph[current_id].push_back(edge(rand() % (int)info.size(), 0));
        }

        if (i % 1000 == 0) // Show status
        {
            printf("%lf percents of graph made\n", (double)i * 100.0 / (double)info.size());
        }


    }
}


void bfs(int x) 
{
    queue<int> q;
    q.push(x);

    while (!q.empty()) 
    {
        int u = q.front();
        q.pop();

        double could_add = (double)info[u].pageRank / (double)graph[u].size();

        for (int i = 0; i < (int)graph[u].size(); i++)
        {
            int v = graph[u][i].to;
            double cur_flow = graph[u][i].flow;

            if (could_add - cur_flow > EPS) 
            {

                info[v].pageRank += could_add - cur_flow;

                graph[u][i].change_flow(could_add);

                if ((int)q.size() < 1000) 
                {
                    q.push(v);
                }
            }
            
        }

        if (finished) 
        {
            break;
        }

    }

}



void calc_pageRanking() 
{
    for (int i = 0; i < (int)graph.size(); i++) 
    {
        printf("Now we are trying to bfs from id = %d\n", i);
        bfs(i);
        if (finished) 
        {
            break;
        }

    }

    printf("Rating already finished.\n Press Enter.\n");
}


void wait() 
{
    char c;

    while (42) 
    {
        c = getchar(); 

        if (c == 10) 
        {
            finished = true;
            break;
        }
    }
}


void save_graph() 
{
    FILE *out = fopen("../../url_graph.dat", "w");

    for (int i = 0; i < (int)graph.size(); i++) 
    {
        for (int j = 0; j < (int)graph[i].size(); j++) 
        {
            fprintf(out, "%d %d %lf\n", i, graph[i][j].to, graph[i][j].flow);
        }
    }
    
    fclose(out);
}

void read_graph() 
{
    FILE *in = fopen("../../url_graph.dat", "r");
    int u, v;
    double flow;
    graph.resize((int)info.size());

    while (fscanf(in, "%d %d %lf\n", &u, &v, &flow) != EOF)
    {
        graph[u].push_back(edge(v, flow));
    }

    fclose(in);
}


int main(int argc, char *argv[]) 
{
    srand(time(NULL));
    if (argc == 2) 
    {
        html_dir = argv[0];
        text_dir = argv[1];
    } else 
    {
        html_dir = "../../html_files/";
        text_dir = "../../text_files/";
    }

    articles = fopen((text_dir + "articles.txt").c_str(), "a+");
    read_info();

    //remove_all_copies(html_dir);
   /* thread Parser(Parse_all_files); 
    wait();


    Parser.join();

    finished = false;
    make_graph();
    save_graph();
*/
    graph.clear();
    
    read_graph();
    thread ranking(calc_pageRanking);
    wait();

    ranking.join();
    save_graph();
    save_info();
    
    fclose(articles);
    return 0;
}
