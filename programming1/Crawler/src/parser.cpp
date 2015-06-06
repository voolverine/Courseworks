#include <vector>
#include <string>
#include <cstdio>
#include <algorithm>

using namespace std;

const string download_dir = "../../html_files/";
const string TEMPLATE = "<a href=\"/wiki/";
const string MAIN_TITLE_CLASS = "firstHeading";


bool compare(const string &main, const string &what, int from) 
{
    for (int i = 0; i < (int)what.size() && i + from < (int)main.size(); i++) 
    {
        if (main[from+ i] != what[i]) 
        {
            return false;
        }
    }

    return true;
}


void find_urls(vector<string> &urls, string s) 
{
    string temp = "";
    for (int i = 0; i < (int)s.size(); i++) 
    {
        if (s[i] == TEMPLATE[0]) 
        {
            temp = "";

            if (compare(s, TEMPLATE, i)) 
            {
                bool trash = false;

                for (int j = (int)TEMPLATE.size(); j + i < (int)s.size(); j++) 
                {
                    if (s[i + j] == '\"' || s[i + j] == '\n') 
                    {
                        break;
                    }

                    if (s[i + j] == ':' || s[i + j] == '%') 
                    {
                        trash = true;
                        break;
                    }

                    temp += s[i + j];
                }

                if (!trash) 
                {
                    if (temp.size() != 0) {
                        urls.push_back(temp);
                    }
                }

            }
        }

    }
}


string get_title(string line) 
{
    string title = "";

    bool main_title_here = false;
    for (int i = 0; i < (int)line.size(); i++) 
    {   
        if (compare(line, MAIN_TITLE_CLASS, i)) 
        {
            main_title_here = true;
            break;
        }
    }

    if (main_title_here) 
    {
        bool between_tags = false;
        for (int i = 0; i < (int)line.size() - 1; i++) // -1 to remove \n
        {
            if (line[i] == '<') 
            {
                between_tags = false;
            }
            if (between_tags) 
            {
                title += line[i];
            }
            if (line[i] == '>') 
            {
                between_tags = true;
            }
        }
    }

    return title;
}

string get_url_from_title(string article_name) 
{
    for (int i = 0; i < (int)article_name.size(); i++) 
    {
        if (article_name[i] == ' ') 
        {
            article_name[i] = '_';
        }
    }

    return article_name;
}


string get_article_url(string filename) 
{
    string article_name = "";
    FILE *html_file = NULL;
    html_file = fopen(filename.c_str(), "r");

    if (html_file != NULL) 
    {
        char buff[1000];
        while (fgets(buff, 1000, html_file) != NULL) 
        {
            article_name = get_title(buff);
            if (article_name != "") 
            {
                break;
            }
        }

        fclose(html_file);
    }

    return get_url_from_title(article_name);
}


vector<string> parse(string filename) 
{
    if (filename == "") 
    {
        filename = "index.html";
    }

    filename = download_dir + filename; 

    vector<string> urls;

    FILE *html_file = NULL;
    html_file = fopen(filename.c_str(), "r");
   
    char buff[500];

    if (html_file != NULL) 
    {
        while (fgets(buff, 500, html_file) != NULL)  
        {
            find_urls(urls, buff);
        }

        fclose(html_file);
    }

    return urls;
}
