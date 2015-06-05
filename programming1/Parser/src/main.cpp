#include <cstdio>
#include <string>
#include <string.h>
#include <vector>
#include <algorithm>
#include <stack>
#include <map>
#include <thread>

#include "text_parser.h"


using namespace std;

string html_dir;
string text_dir;

bool finished = false;


class article 
{
    public:
        long int from, to;
        string url;
        double pageRank;

    article(long int from, long int to, string url,
                double pageRank = 0): from(from), to(to), url(url), pageRank(pageRank) {}

};


map<pair<long long, long long>, int> saved; // url_hash -> id
vector<article> info; // id -> article info

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


void save_text(string &title, string &text, string url) 
{
    fseek(articles, 0, SEEK_END); 

    long int from = ftell(articles);
    saved[getHash(url)] = (int)info.size() - 1;

    fprintf(articles, "%s\n\n", title.c_str());
    fprintf(articles, "%s\n", text.c_str()); 

    long int to = ftell(articles);
    info.push_back(article(from, to, url));    
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
    long int from, to;
    char buff[200];

    while (fscanf(id, "%ld %ld %s", &from, &to, buff) != EOF) 
    {
        if (!already_saved(buff)) 
        {
            info.push_back(article(from, to, buff));
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
        fprintf(id, "%ld %ld %s\n", info[i].from, info[i].to, info[i].url.c_str()); 
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



int main(int argc, char *argv[]) 
{
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
    thread Parser(Parse_all_files); 

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

    Parser.join();

    save_info();

    fclose(articles);
    return 0;
}
