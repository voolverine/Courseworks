#include <cstdio>
#include <cstdlib>
#include <string>
#include <queue>
#include <vector>
#include <ctime>
#include <thread>
#include <set>

#include "parser.h"
#include "checker.h"

using namespace std;

const string PREFIX = "simple.wikipedia.org/wiki/";

queue<string> urls_to_save;
set<pair<long long, long long> > in_queue;
bool finished = false;


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


void sleep(int delay) 
{
    double goal = (double)delay / 1000.0 + (double)clock() / CLOCKS_PER_SEC;
    while (goal > (double)clock() / CLOCKS_PER_SEC) {}
}


void crawl() 
{
    while (!urls_to_save.empty()) 
        {
            string page_title = urls_to_save.front();
            string current_url = PREFIX + page_title; 
            urls_to_save.pop();

            string system_query = "wget --directory-prefix=../Downloads/ ";
            system_query += current_url;
            system(system_query.c_str());
            mark_as_looked(page_title);

            vector<string> possible_urls = parse(page_title);

            push_in_queue(possible_urls);

            sleep(500);

            if (finished) 
            {
                return;
            }

        }
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
            urls.push_back(buff);
        }

        push_in_queue(urls);
    }

    fclose(in);
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
}


int main() 
{
    read_all_visited();
    read_queue();

    if (urls_to_save.empty()) 
    {
        urls_to_save.push("");
    }


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

