#include <string>
#include <vector>
#include <cmath>
#include <set>

#include "../../hashlib/hash.h"

using namespace std;


void to_low(string &word) 
{
    for (int i = 0; i < (int)word.size(); i++) 
    {
        if (word[i] >= 'A' && word[i] <= 'Z') 
        {
            word[i] = word[i] - 'A' + 'a';
        }

    }
}


int get_count_of_word(string word, string file) 
{
    // Use z-function to enhance performance
    
    int len = (int)word.size();
    int count = 0;
    to_low(word);
    to_low(file);
    string s = word + "#$#" + file;
    bool still_in_header = true;

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

        if (s[i] == '\n') 
        {
            still_in_header = false;  // Header more important
        }


        if (z[i] == len) 
        {
            count += (still_in_header) ? 10 : 1;
        }
	}

    return count;
}

vector<string> get_dif_words_in_article(string s) 
{
    set<pair<long long, long long> > used;
    vector<string> ans;
    to_low(s);

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
