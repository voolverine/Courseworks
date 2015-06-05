#include <string>
#include <string.h>
#include <vector>
#include <algorithm>


using namespace std;

const string MAIN_TITLE_CLASS = "firstHeading";
const string PREFIX = "<div id=\"mw-content-text\"";
const string DIV_START = "<div";
const string TABLE_START = "<table";
const string TABLE_END = "</table>";
const string DIV_END = "</div";
const string H2 = "<h2>";


bool find_prefix(const string &prefix, string &s, int p) 
{
    for (int i = p; i < (int)s.size() && i - p < (int)prefix.size(); i++) {
        if (s[i] != prefix[i - p]) {
            return false;
        }
    }

    return true;
}


string get_main_text(string file) 
{
    bool in = false;
    string result = "";
    int div_start = 0, div_end = 0;
    int table_start = 0, table_end = 0;

    for (int i = 0; i < (int)file.size(); i++) 
    {
        if (!in && find_prefix(PREFIX, file, i)) 
        {
            in = true;
        }
        if (in && find_prefix(DIV_START, file, i)) 
        {
            div_start++; 
        }
        if (in && find_prefix(DIV_END, file, i))
        {
            div_end++;
        }
        if (in && div_end == div_start) 
        {
            break;
        }
        if (find_prefix(TABLE_START, file, i)) 
        {
            table_start++; 
        }
        if (find_prefix(TABLE_END, file, i)) 
        {
            table_end++; 
        }


        if (in && div_end == (div_start - 1) && table_start - table_end <= 0) 
        {
            result += file[i];
        }
    }

    return result;
}


string remove_tags(string &file) 
{
    string result = "";
    bool in_tag = false;
    for (int i = 0; i < (int)file.size(); i++) 
    {
        if (file[i] == '<')
        {
            in_tag = true;
        }
        if (in_tag && find_prefix(H2, file, i)) 
        {
            result += '\n';
        }

        if (!in_tag) 
        {
            result += file[i];
        }
        if (file[i] == '>')
        {
            in_tag = false;
        }
    }
    return result;
}


string remove_buttons(string &file) 
{
    string result = "";
    bool in_button = false;
    for (int i = 0; i < (int)file.size(); i++) 
    {
        if (file[i] == '[') 
        {
            in_button = true;
        }
        if (!in_button) 
        {
            result += file[i];
        }
        if (file[i] == ']') 
        {
            in_button = false;
        }
    }

    return result;
}


string get_title(string line) 
{
    string title = "";
    if ((int)line.size() == 0) 
    {
        return title;
    }

    int i;
    for (i = 0; i < (int)line.size(); i++) 
    {   
        bool main_title_here = true;
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

    bool between_tags = false;
    for (; i < (int)line.size() - 1; i++) // -1 to remove \n
    {
        if (line[i] == '<') 
        {
            between_tags = false;
            break;                // that because we have all file in one string
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

    return title;
}


string get_url_from_title(string s) 
{
    for (int i = 0; i < (int)s.size(); i++) 
    {
        if (s[i] == ' ') 
        {
            s[i] = '_';
        }
    }

    return s;
}


void remove_whitelines(string &s) 
{
    int beg = 0;
    int end = s.size() - 1;
    while (beg < (int)s.size() && s[beg] == '\n') 
    {
        beg++;
    }
    while (end >= 0 && s[end] == '\n') 
    {
        end--;
    }

    s = s.substr(beg, end - beg + 1);
}



pair<string, string> parse(string filename) 
{
    FILE *html = NULL;
    html = fopen(filename.c_str(), "r");

    char buf[1000];
    string file = "";
    while (!feof(html) && fgets(buf, 1000, html)) 
    {
        file += buf;
    }
    
    string title = get_title(file);
    file = get_main_text(file);
    file = remove_tags(file); 
    file = remove_buttons(file);
    remove_whitelines(file);
    
    fclose(html);
    return make_pair(title, file);
}
