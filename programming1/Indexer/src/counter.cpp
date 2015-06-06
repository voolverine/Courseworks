#include <string>
#include <vector>
#include <cmath>
#include <set>

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


int get_count_of_word(string word, string file) 
{
    // Use z-function to enhance performance
    
    int len = (int)word.size();
    int count = 0;

    string s = word + "#$#" + file;

	int n = (int) s.length();
	vector<int> z (n);

	for (int i = 1, l = 0, r = 0; i < n; i++) 
    {
		if (i <= r) 
        {
			z[i] = min(r - i + 1, z[i - l]);
        }
		while (i + z[i] < n && s[z[i]] == s[i + z[i]]) 
        {
			z[i]++;
        }
		if (i + z[i] - 1 > r) {
			l = i;
            r = i + z[i] - 1;
        }

        if (z[i] == len) 
        {
            count++;
        }
	}

    return count;
}

vector<string> get_dif_words_in_article(string s) 
{
    set<pair<long long, long long> > used;
    vector<string> ans;

    string temp = "";
    for (int i = 0; i < (int)s.size(); i++) 
    {
        if (s[i] == ' ' || s[i] == '\n' || s[i] == '.' || s[i] == ','
                || s[i] == ':' || s[i] == '-') 
        {
            if (temp.size() > 0) 
            {
                pair<long long, long long> hash = getHash(temp);

                if (used.find(hash) == used.end()) 
                {
                    ans.push_back(temp);
                    used.insert(hash);
                }
                temp = "";
            }
        } else 
        {
            temp += s[i];
        }
    }

    return ans;
}
