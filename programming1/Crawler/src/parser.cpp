#include <vector>
#include <string>
#include <cstdio>
#include <algorithm>

using namespace std;

const string TEMPLATE = "<a href=\"/wiki/";
const string MAIN_TITLE_CLASS = "firstHeading";

void find_urls(vector<string> &urls, string s) 
{
    string temp = "";
    for (int i = 0; i < (int)s.size(); i++) 
    {
        if (s[i] == TEMPLATE[0]) 
        {
            temp = "";
            bool f = true;

            for (int j = 0; j < (int)min(TEMPLATE.size(), s.size()); j++) 
            {
                if (TEMPLATE[j] != s[i + j]) 
                {
                    f = false;
                    break;
                }
            }

            if (f) 
            {
                bool trash = false;

                for (int j = (int)TEMPLATE.size(); j + i < (int)s.size(); j++) 
                {
                    if (s[i + j] == '\"') 
                    {
                        break;
                    }

                    if (s[i + j] == ':') 
                    {
                        trash = true;
                        break;
                    }

                    temp += s[i + j];
                }

                if (!trash) 
                {
                    urls.push_back(temp);
                }
            }
        }
    }
}

string get_title(string line) 
{
    string title = "";
    if ((int)line.size() == 0) 
    {
        return title;
    }

    bool main_title_here;
    for (int i = 0; i < (int)line.size(); i++) 
    {   
        main_title_here = true;
        for (int j = 0; i + j < (int)line.size() && j < (int)MAIN_TITLE_CLASS.size(); j++) 
        {
            if (line[i + j] != MAIN_TITLE_CLASS[j]) 
            {
                main_title_here = false;
                break;
            }
        }
        if (main_title_here) 
        {
            break;
        }
    }

    if (main_title_here) 
    {
        bool between_tags = false;
        for (int i = 0; i < (int)line.size(); i++) 
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




vector<string> parse(string filename) 
{
    if (filename == "") 
    {
        filename = "index.html";
    }

    filename = "../Downloads/" + filename; 

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
