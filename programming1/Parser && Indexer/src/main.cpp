#include <cstdio>
#include <string>
#include <string.h>
#include <vector>
#include <algorithm>
#include <stack>

#include "text_parser.h"


using namespace std;

string html_dir;
string text_dir;


vector<string> get_filenames_in_dir(string dir) 
{
    vector<string> result;

    FILE *names;
    names = popen(("ls -a1 " + dir).c_str(), "r");
    
    char buff[1000];
    while (!feof(names) && fgets(buff, 1000, names)) 
    {
        string new_file = buff;
        new_file.erase(new_file.end() - 1, new_file.end());     // remove \n at the end
        result.push_back(new_file);
    }

    return result;
}


void save_text(string &title, string &text, string &filename) 
{
    FILE *out = NULL;
    out = fopen((text_dir + filename).c_str(), "w");
    
    if (out != NULL) 
    {
        fprintf(out, "%s\n", title.c_str()):
        fprintf(out, "%s", text.c_str());
        fclose(out);
    }

    return;
}



void Parse_all_files() 
{
    vector<string> all_files = get_filenames_in_dir(html_dir);

    for (int i = 0; i < (int)all_files.size(); i++) 
    {
        string text = parse(all_files[i]);
        save_text(text.first, text.second, all_files[i]);
    }

    return;
}



int main(int argc, char *argv[]) 
{
    if (argc == 2) 
    {
        html_dir = argv[0];
        text_dir = argv[1];
    } else 
    {
        html_dir = "../html_files/";
        text_dir = "../text_files/";
    }

    Parse_all_files();


    return 0;
}
