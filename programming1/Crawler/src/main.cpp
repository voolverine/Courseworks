#include <cstdio>
#include <cstdlib>
#include <string>
#include <string.h>
#include <queue>
#include <vector>
#include <ctime>
#include <thread>
#include <set>
#include <sys/statvfs.h>

#include "parser.h"
#include "checker.h"

using namespace std;

const string PREFIX = "simple.wikipedia.org/wiki/";
const string RANDOM = "simple.wikipedia.org/wiki/Special:Random";
const unsigned int MAX_QUEUE_SIZE = 1000000;

queue<string> urls_to_save;
set<pair<long long, long long> > in_queue;
bool finished = false;


void sleep(int delay) 
{
    double goal = (double)delay / 1000.0 + (double)clock() / CLOCKS_PER_SEC;
    while (goal > (double)clock() / CLOCKS_PER_SEC) {}
}


double free_space(string path) 
{
    struct statvfs info;

    if (statvfs(path.c_str(), &info) != -1) 
    {
        return (double)info.f_bsize * info.f_bfree / (1024 * 1024);
    }

    return 10000; // I hope it will never execute
}


bool is_in_queue(string &s) 
{
    pair<long, long> hash = getHash(s);
    if (in_queue.find(hash) != in_queue.end()) 
    {
        return true;
    }
    return false;
}


void push_in_queue(vector<string> &urls) 
{
    if (urls_to_save.size() > MAX_QUEUE_SIZE) 
    {
        return; // trying to escape overflow
    }

    for (int i = 0; i < (int)urls.size(); i++) 
    {   
        if (check(urls[i]) && !(is_in_queue(urls[i]))) 
        {
            urls_to_save.push(urls[i]);
            in_queue.insert(getHash(urls[i]));
        }
    }

    return;
}


string get_random_article() 
{   
    system(("wget --directory-prefix=../Downloads/temp " + RANDOM).c_str());
//    sleep(500);
    
    string article_url= get_article_url("../Downloads/temp/Special:Random");

    system("rm -rf ../Downloads/temp/*");  
    return article_url;
}


void search_something_new() 
{
    while (urls_to_save.size() == 0) 
    {
        vector<string> urls = vector<string> (1, get_random_article()); 
        push_in_queue(urls);

        if (finished) 
        {
            return;
        }
    }

    return;
}


void crawl() 
{
    int errors = 0; 
    while (42) 
    {
        if (urls_to_save.size() == 0) 
        {
            search_something_new();
        }
        if (finished || free_space(".") < 5000 || errors > 500) 
        {
            return;
        }

        string page_title = urls_to_save.front();
        string current_url = PREFIX + page_title; 
        urls_to_save.pop();

        string system_query = "wget --directory-prefix=../Downloads/ ";
        system_query += current_url;
        int query = system(system_query.c_str());
        
        mark_as_looked(page_title);

        vector<string> possible_urls = parse(page_title);
        push_in_queue(possible_urls);

        if (query != 0) {
            errors++;       
        } else 
        {
            errors = 0;
        }
        //sleep(500);
    }

    return;
}


void read_queue() 
{
    FILE *in = NULL;
    in = fopen("../Downloads/queue", "r");

    if (in != NULL) 
    {
        char buff[500];
        vector<string> urls;

        while (fgets(buff, 500, in) != NULL) 
        {
            buff[strlen(buff) - 1] = '\0';
            urls.push_back(buff); 
        }

        push_in_queue(urls);
    }

    fclose(in);
    return;
}


void save_queue() 
{
    FILE *out = NULL;
    out = fopen("../Downloads/queue", "w");

    while (!urls_to_save.empty()) 
    {
        fputs((urls_to_save.front() + '\n').c_str(), out);
        urls_to_save.pop();
    }

    fclose(out); 
    return;
}


int main() 
{
    read_all_visited();
    read_queue();

    thread crawler(crawl);    

    char c;
    while (42) 
    {
        c = getchar();

        if (c == 10) 
        {
            finished = true; // Press Enter to Finish
            break;
        }
    }

    crawler.join();

    if (!urls_to_save.empty()) 
    {
        save_queue();
    }

    return 0;
}

