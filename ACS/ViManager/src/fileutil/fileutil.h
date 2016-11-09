#include <string.h>
#include <string>
#include <unistd.h>
#include <stdio.h>
#include <dirent.h>
#include <vector>


struct File {
    std::string filename;
    unsigned char d_type;
};


std::string get_exec_path();
std::string get_exec_user();
std::vector<File> get_files_in_dir(const std::string &path);
std::vector<std::string> split_path(const std::string &path);
std::string join_path(const std::vector<std::string> &v);
