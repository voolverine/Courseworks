#include <algorithm>
#include <cstdio>
#include <string>
#include <set>

using namespace std;
const long long MOD1 = 1073676287LL;
const long long MOD2 = 2971215073LL;
const long long base = 241;

FILE* data = NULL;

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
    if (data != NULL) 
    {
        fprintf(data, "%lld %lld\n", hash.first, hash.second);    
    }
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


string ll_to_str(long long value) 
{
    string result = "";
    while (value > 0) 
    {
        result += (value % 10) + '0';
        value /= 10;
    }
    reverse(result.begin(), result.end());
    return result;
}


void read_all_visited() 
{
    looked.clear();
    
    data = fopen("../Downloads/visited", "a+");
   
    if (data != NULL) 
    {
        char buff[100];
        while (fgets(buff, 100, data) != NULL) 
        {
            long long first_hash, second_hash;
            sscanf(buff, "%lld %lld", &first_hash, &second_hash);
            looked.insert(make_pair(first_hash, second_hash));
        }
    }

    fflush(data);
}
