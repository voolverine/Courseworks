#include <algorithm>
#include <cstdio>
#include <string>
#include <string.h>
#include <set>

using namespace std;
const long long MOD1 = 1073676287LL;
const long long MOD2 = 2971215073LL;
const long long base = 241;


set<pair<long long, long long> > looked;


pair<long long, long long> getHash(string s) 
{
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


void mark_as_looked(string url) 
{
    pair<long long, long long> hash = getHash(url);
    looked.insert(hash);
}


bool if_not_looked(string url) 
{
    pair<long long, long long> hash = getHash(url);
    if (looked.find(hash) == looked.end()) 
    {
        return true;
    } else 
    {
        return false;
    }
}


bool check(string url) 
{
    if (!if_not_looked(url)) 
    {
        return false;
    }

    return true;
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
        new_file.erase(new_file.end() - 1, new_file.end());     // remove \n at the end
        result.push_back(new_file);
    }

    return result;
}


void read_all_visited() 
{
    vector<string> all_files = get_filenames_in_dir("../Downloads/");
    looked.clear();
   
    for (int i = 0; i < (int)all_files.size(); i++) 
    {
        mark_as_looked(all_files[i]);
    }
}
