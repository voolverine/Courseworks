#include <vector>
#include <string>
#include <cstdio>
#include <algorithm>

using namespace std;

const string TEMPLATE = "<a href=\"/wiki/";


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


vector<string> parse(string filename) 
{
    if (filename == "") 
    {
        filename = "index.html";
    }

    filename = "../Downloads/" + filename; 

    vector<string> urls;

    FILE* html_file = NULL;
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
