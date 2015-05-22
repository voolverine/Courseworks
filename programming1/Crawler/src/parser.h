#include <vector>
#include <string>

using namespace std;

string get_title(string line);
string get_url_from_title(string article_name);
vector<string> parse(string filename);

void mark_as_looked(string url);
