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


size_t find(const std::vector<File> &files, const std::string &filename) {
    for (size_t i = 0; i < files.size(); i++) {
        if (files[i].filename == filename) {
            return i;
        }
    }

    return files.size();
}

bool is_dir(const std::vector<File> &files, const std::string &selected) {
    size_t selected_index = find(files, selected);
    return selected_index < files.size()
                         && files[selected_index].d_type == DT_DIR; 
}


std::string directory_size(std::string path) {
    DIR *dir = opendir(path.c_str());
    struct dirent *current;
    int ans = 0;

    if (dir != nullptr) {
        while (current = readdir(dir)) {
            ans++;
        }
        closedir(dir);
    }

    ans -= 2;
    std::string result = "";
    while (ans > 0) {
        result.push_back(ans % 10 + '0');
        ans /= 10;
    }


    if (result.size() == 0) {
        result.push_back('0');
    }
    reverse(result.begin(), result.end());
    return result;
}


std::string to_string(int x) {
    std::string result = "";

    while (x > 0) {
        result += (x % 10) + '0';
        x /= 10;
    }

    if (result == "") {
        return "0";
    }
    std::reverse(result.begin(), result.end());
    return result;
}


std::string format_size(int64_t size) {
    int first = 0;
    bool dot = false;
    int second = 0;
    std::string ending = "";

    if (size < 1024)  {
        first = size;
        ending = " B";
    } else if (1024 <= size && size < 1048576) {
        first = size / 1024;
        if (size % 1024 != 0) {
            dot = true;
            second = size % 1000;
        }
        ending = " K";
    } else if (1048576 <= size && size < 1073741824){
        size /= 1024;
        first = size / 1024;
        if (size % 1024 != 0) {
            dot = true; 
            second = size % 1000;
        }
        ending = " M";
    } else {
        size /= 1048576;
        first = size / 1024;
        if (size % 1024 != 0) {
            dot = true; 
            second = size % 1000;
        }
        ending = " G";
    }

    std::string formatted = to_string(first);
    if (dot) {
        formatted += "." + to_string(second);
    }
    formatted += ending;

    return formatted;
}


std::string file_size(std::string path) {
    FILE *file = nullptr;     
    file = fopen(path.c_str(), "rb");
    fseek(file, 0, SEEK_END);
    int64_t size = ftell(file);
    fclose(file);

    return format_size(size);
}


std::string get_file_permissions(std::string path) {
    struct stat results; 
    stat(path.c_str(), &results);
    std::string special = "-";
    std::string owner = "";
    std::string group = "";
    std::string other = "";

    if (results.st_mode & S_IFDIR) {
        special = "d";
    } else if (S_ISLNK(results.st_mode)) {
        special = "l";
    } else if (results.st_mode & S_ISUID) {
        special = "s";
    } else if (results.st_mode & S_ISVTX) {
        special = "t";
    }

    std::vector<int> owner_masks = {S_IRUSR, S_IWUSR, S_IXUSR};
    std::vector<int> group_masks = {S_IRGRP, S_IWGRP, S_IXGRP};
    std::vector<int> other_masks = {S_IROTH, S_IWOTH, S_IXOTH};
    std::vector<std::string> symb = {"r", "w", "x"};

    for (size_t i = 0; i < owner_masks.size(); i++) {
        if (results.st_mode & owner_masks[i]) {
            owner += symb[i];
        } else {
            owner += "-";
        }

        if (results.st_mode & group_masks[i]) {
            group += symb[i];
        } else {
            group += "-";
        }

        if (results.st_mode & other_masks[i]) {
            other += symb[i];
        } else {
            other += "-";
        }
    }

    std::string result = special + owner + group + other;

    return result;
}


std::string get_file_other_info(std::string path) {
    struct stat results; 
    stat(path.c_str(), &results);

    std::string result = "";
    struct passwd *pw = getpwuid(results.st_uid);
    struct group  *gr = getgrgid(results.st_gid);

    if (pw != nullptr) {
        result += " " + std::string(pw -> pw_name);
    }
    if (gr != nullptr) {
        result += " " + std::string(gr -> gr_name);
    }
/*
    struct tm * timeinfo = localtime(&results.st_ctime); 
    if (timeinfo != nullptr) {
        result += " " + std::string(asctime(timeinfo));
    }
*/
}
