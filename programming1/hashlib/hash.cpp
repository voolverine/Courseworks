#include <algorithm>

using namespace std;

const long long MOD1 = 1073676287LL;
const long long MOD2 = 2971215073LL;
const long long base = 241;


pair<long long, long long> getHash(string s) 
{
    while (s.size() > 0 && s[(int)s.size() - 1] == '\n') 
    {
        s = s.substr(0, (int)s.size() - 1);
    }

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

    hash1 += 100LL*MOD1;
    hash1 %= MOD1;
    hash2 += 100LL*MOD2;
    hash2 %= MOD2;
    return make_pair(hash1, hash2);
}
