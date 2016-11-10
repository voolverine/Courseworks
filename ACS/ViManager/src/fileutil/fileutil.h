#include <string.h>
#include <string>
#include <unistd.h>
#include <stdio.h>
#include <dirent.h>
#include <vector>


struct File {
    std::string filename;
    unsigned char d_type;

    const static bool usual_order_cmp(const File &lhs, const File &rhs) {
        if (lhs.d_type != rhs.d_type) {
            if (lhs.d_type == DT_DIR) {
                return true;
            } else {
                return false;
            }
        }

        return lhs.filename < rhs.filename;
    }
};


std::string get_exec_path();
std::string get_exec_user();
std::vector<File> get_files_in_dir(const std::string &path);
std::vector<std::string> split_path(const std::string &path);
std::string join_path(const std::vector<std::string> &v);
size_t find(const std::vector<File> &files, const std::string &filename);
bool is_dir(const std::vector<File> &files, const std::string &selected);

