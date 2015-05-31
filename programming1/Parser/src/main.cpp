#include <cstdio>
#include <string>
#include <string.h>
#include <vector>
#include <algorithm>
#include <stack>


using namespace std;

const string  PREFIX = "<div id=\"mw-content-text\"";
const string DIV_START = "<div";
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
        if (in) 
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


void parse(string s) 
{
    FILE *html = NULL;
    html = fopen(s.c_str(), "r");

    char buf[1000];
    string file = "";
    while (!feof(html) && fgets(buf, 1000, html)) 
    {
        file += buf;
    }

    file = get_main_text(file);
    file = remove_tags(file); 
    file = remove_buttons(file);
    printf("%s", file.c_str());
}


int main() 
{
    parse("Superga");
    return 0;
}

