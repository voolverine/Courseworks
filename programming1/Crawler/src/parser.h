#include <vector>
#include <string>

using namespace std;

string get_title(string line);
string get_article_url(string filename);
vector<string> parse(string filename);

void mark_as_looked(string url);
