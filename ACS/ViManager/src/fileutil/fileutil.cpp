#include "fileutil.h"

std::string get_exec_path() {
    char the_path[256];

    getcwd(the_path, 255);
    return std::string(the_path);
}


std::string get_exec_user() {
    char login[256];

    getlogin_r(login, 255);
    return std::string(login);
}


std::vector<File> get_files_in_dir(const std::string &path) {
    std::vector<File> result;
    DIR *dir = opendir(path.c_str());
    struct dirent *current;

    if (dir != nullptr) {
        while (current = readdir(dir)) {
            File new_file;
            new_file.filename = std::string(current -> d_name);
            new_file.d_type = current -> d_type;
            result.push_back(new_file);
        }

        closedir(dir);
    }

    return result;
}


std::vector<std::string> split_path(const std::string &path) {
    std::vector<std::string> result;
    std::string current = "/";

    for (size_t i = 0; i < path.size(); i++) {
        if (path[i] == '/') {
            result.push_back(current);
            current = "";
        } else {
            current += path[i];
        }
    }

    if (current.size() != 0) {
        result.push_back(current);
    }

    return result;
}


std::string join_path(const std::vector<std::string> &v) {
    std::string result = "";
    for (size_t i = 0; i < v.size(); i++) {
        result += v[i];
        if (i > 0 && i < (int)v.size() - 1) {
            result += '/';
        }
    }

    return result;
}
