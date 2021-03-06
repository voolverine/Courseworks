#include <iostream>
#include <stack>
#include <vector>
#include <map>
#include <algorithm>

#include "ncurses.h"
#include "gui/gui.h"
#include "fileutil/fileutil.h"


WIN address_win;
WIN left_win;
WIN main_win;
WIN right_win;
WIN command_win;

int current_mode = NORMAL_MODE;

/* PRIORITY KEY FAST CODE ;( */
int mode_position = 0;
int selected_line = 0;
std::string mode_string = "";
std::string ownerS = "Owner:";
std::string groupS = "Group:";
std::string current_owner = "";
std::string current_group = "";
std::string *current;


void log(std::string str) {
    FILE *file = fopen("log.log", "a");

    if (file != nullptr) {
        fprintf(file, "%s\n", str.c_str());
        fclose(file);
    }
}


void initialize_main_windows(WIN *address_win,
                             WIN *left_win,
                             WIN *main_win,
                             WIN *right_win,
                             WIN *command_win) {

    int main_win_width = COLS * 3 / 8;
    int right_win_width = COLS * 4 / 8;
    int left_win_width = COLS - main_win_width - right_win_width;
    int frame_height = LINES - 2;

    int left_win_startx = 0;
    int left_win_starty = 1;
    int main_win_startx = left_win_startx + left_win_width;
    int main_win_starty = 1;
    int right_win_startx = main_win_startx + main_win_width;
    int right_win_starty = 1;

    init_win_params(address_win, 1, COLS - 1, 0, 0);
    init_win_params(command_win, 1, COLS - 1, 0, LINES - 1);
    init_win_params(left_win, frame_height, left_win_width,
                        left_win_startx, left_win_starty);
    init_win_params(main_win, frame_height, main_win_width,
                        main_win_startx, main_win_starty);
    init_win_params(right_win, frame_height, right_win_width,
                        right_win_startx, right_win_starty);


    bool border = false;
    create_box(address_win, border);
    create_box(command_win, border);
    create_box(left_win, border);
    create_box(main_win, border);
    create_box(right_win, border);
}


void initialize_state(std::vector<std::string> *current_path,
                        std::vector<File> *previous_step_files,
                        std::vector<File> *current_step_files,
                        std::vector<File> *next_step_files,
                        std::string *selected) {

    previous_step_files -> clear();
    current_step_files -> clear();
    next_step_files -> clear();

    if (current_path -> size() > 1) {
        std::string temp = current_path -> back();
        current_path -> pop_back();
        *previous_step_files =
            std::move(get_files_in_dir(join_path(*current_path)));
        current_path -> push_back(temp);
    }
    *current_step_files = std::move(get_files_in_dir(join_path(*current_path)));
    sort((*previous_step_files).begin(), (*previous_step_files).end(),
            File::usual_order_cmp);
    sort((*current_step_files).begin(), (*current_step_files).end(),
            File::usual_order_cmp);
    *selected = ".";
}


void update_win(WIN *win, const std::string &current_path,
        std::vector<File> *files, const std::string &selected, bool sizes) {
    create_box(win, false);
    if (files -> size() == 0) {
        return;
    }
    int page_size = LINES - 2;
    int selected_index = find(*files, selected);
    int from = 0;
    int to = std::min(page_size - 1, (int)files->size() - 1);
    int shift = 0;

    if (selected_index >= page_size) {
        shift = selected_index - page_size + 1;
    }

    from += shift;
    to += shift;

    for (int i = from; i <= to; i++) {
        auto file = (*files)[i];
        if (file.d_type == DT_DIR) {
            if (sizes) {
                std::string folder_path = current_path;
                if (folder_path.size() > 1) {
                    folder_path.push_back('/');
                }

                folder_path += file.filename;
                win -> writeRightC(directory_size(folder_path), BLUE_ON_BLACK);
                win -> writeC(file.filename, false, BLUE_ON_BLACK);
                win -> crop_to_next_line();
            } else {
                win -> writeLineC(file.filename, false, BLUE_ON_BLACK);
            }
        } else {
            if (sizes) {
                std::string file_path = current_path;
                if (file_path.size() > 1) {
                    file_path.push_back('/');
                }

                file_path += file.filename;

                win -> writeRight(file_size(file_path));
                win -> write(file.filename, false);
                win -> crop_to_next_line();
            } else {
                win -> writeLine(file.filename, false);
           }
        }
    }

    int color = BLACK_ON_YELLOW;
    bool folder = false;
    if ((*files)[selected_index].d_type == DT_DIR) {
        color = BLACK_ON_BLUE;
        folder = true;
    }

    win -> set_line(selected_index - shift);
    win -> writeC((*files)[selected_index].filename, false, color);
    for (int i = 0; i < 200; i++) {
        win -> writeC(" ", false, color);
    }
    if (sizes) {
        if (folder) {
            std::string folder_path = current_path;
            if (folder_path.size() > 1) {
                folder_path.push_back('/');
            }

            folder_path += selected;

            win -> writeRightC(directory_size(folder_path), color);
            win -> crop_to_next_line();
        } else {
            std::string file_path = current_path;
            if (file_path.size() > 1) {
                file_path.push_back('/');
            }

            file_path += selected;

            win -> writeRightC(file_size(file_path), color);
            win -> crop_to_next_line();
        }
    }

    std::string permission_target = current_path;
    if (permission_target.size() > 1) {
        permission_target.push_back('/');
    }
    permission_target += selected;
    std::string permissions = get_file_permissions(permission_target);
    std::string other_info = get_file_other_info(permission_target);
    create_box(&command_win, false);
    command_win.writeC(permissions, false, BLUE_ON_BLACK);
    command_win.write(other_info, false);
}


void update_address_line(WIN *win, std::vector<std::string> *path,
                                                const std::string &selected) {
    create_box(win, false);
    win -> writeC(get_exec_user() + ":", false, GREEN_ON_BLACK);
    win -> writeC(join_path(*path), false, BLUE_ON_BLACK);
    if (path -> size() > 1) {
        win -> writeC("/", false, BLUE_ON_BLACK);
    }
    win -> writeC(selected, false, WHITE_ON_BLACK);
}


void show_lines_in_win(WIN *win, const std::vector<std::string> &array) {
    create_box(win, false);
    for (size_t i = 0; i < array.size(); i++) {
        win -> writeLine(array[i], true);
    }
}


void show_right_part(std::vector<File> *current_step_files,
                     std::vector<File> *next_step_files,
                     std::vector<std::string> *current_path,
                     const std::string &selected) {
    next_step_files -> clear();
    if (selected == "." || selected == "..") {
        create_box(&right_win, false);
        return;
    }

    int selected_index = find(*current_step_files, selected);
    if ((*current_step_files)[selected_index].d_type == DT_DIR) {
        current_path -> push_back(selected);
        (*next_step_files) =
            std::move(get_files_in_dir(join_path(*current_path)));
        sort(next_step_files -> begin(), next_step_files -> end(),
                File::usual_order_cmp);
        current_path -> pop_back();
        update_win(&right_win, join_path(*current_path),
                next_step_files, ".", false);
    } else {
        current_path -> push_back(selected);
        std::vector<std::string> contains = std::move(
                file_repr(join_path(*current_path)));
        current_path -> pop_back();
        show_lines_in_win(&right_win, contains);
    }
}


void full_update(std::vector<std::string> *current_path,
                        std::vector<File> *previous_step_files,
                        std::vector<File> *current_step_files,
                        std::vector<File> *next_step_files,
                        std::string *selected) {

    initialize_state(current_path, previous_step_files,
            current_step_files, next_step_files, selected);

    if (current_path -> size() > 1) {
        update_win(&left_win, join_path(*current_path),
                previous_step_files, current_path -> back(), false);
    } else {
        create_box(&left_win, false);
    }

    update_win(&main_win, join_path(*current_path),
            current_step_files, *selected, true);
    update_address_line(&address_win, current_path, *selected);
    show_right_part(current_step_files, next_step_files, current_path,
                                                *selected);
}



void left_handler(std::vector<std::string> *current_path,
                        std::vector<File> *previous_step_files,
                        std::vector<File> *current_step_files,
                        std::vector<File> *next_step_files,
                        std::string *selected) {
    if (current_path -> size() > 1) {
        std::string back = current_path -> back();
        current_path -> pop_back();

        initialize_state(current_path, previous_step_files,
                current_step_files, next_step_files, selected);
        *selected = std::move(back);

        update_win(&main_win, join_path(*current_path),
                current_step_files, *selected, true);

        if (previous_step_files -> size() > 0) {
            std::string selected_in_previous = current_path -> back();
            update_win(&left_win, join_path(*current_path),
                    previous_step_files, selected_in_previous, false);
        } else {
            create_box(&left_win, false);
        }
        show_right_part(current_step_files, next_step_files, current_path,
                                                    *selected);
    }
}


void down_handler(std::vector<std::string> *current_path,
                        std::vector<File> *previous_step_files,
                        std::vector<File> *current_step_files,
                        std::vector<File> *next_step_files,
                        std::string *selected) {
    int ind = find(*current_step_files, *selected);
    if (ind + 1 < current_step_files -> size()) {
        ind++;
    }

    *selected = (*current_step_files)[ind].filename;
    update_win(&main_win, join_path(*current_path),
            current_step_files, *selected, true);
    show_right_part(current_step_files, next_step_files, current_path,
                                                *selected);
}


void up_handler(std::vector<std::string> *current_path,
                        std::vector<File> *previous_step_files,
                        std::vector<File> *current_step_files,
                        std::vector<File> *next_step_files,
                        std::string *selected) {
    int ind = find(*current_step_files, *selected);
    if (ind - 1 > -1) {
        ind--;
    }

    *selected = (*current_step_files)[ind].filename;
    update_win(&main_win, join_path(*current_path),
            current_step_files, *selected, true);

    show_right_part(current_step_files, next_step_files, current_path,
                                                *selected);
}


void right_handler(std::vector<std::string> *current_path,
                        std::vector<File> *previous_step_files,
                        std::vector<File> *current_step_files,
                        std::vector<File> *next_step_files,
                        std::string *selected) {
    if (*selected == ".") {
        full_update(current_path, previous_step_files,
                current_step_files, next_step_files, selected);
    } else if (*selected == "..") {
        left_handler(current_path, previous_step_files,
                current_step_files, next_step_files, selected);
    } else if (is_dir(*current_step_files, *selected)) {
        current_path -> push_back(*selected);
        if (have_access_to_read_directory(join_path(*current_path))) {
            log("ACCESS");
            full_update(current_path, previous_step_files,
                    current_step_files, next_step_files, selected);
        } else {
            log("NOACCESS");
            current_path -> pop_back();
        }
    }
}

void priority_mode_draw() {
    create_box(&left_win, false);

    for (int i = 0; i < mode_position && i < mode_string.size(); i++) {
        std::string out = "";
        out += mode_string[i];
        left_win.write(out, false);
    }
    std::string selected = "";
    selected += mode_string[mode_position];
    if (selected_line == 0) {
        left_win.writeC(selected, false, BLACK_ON_BLUE);
    } else {
        left_win.write(selected, false);
    }


    for (int i = mode_position + 1; i < mode_string.size(); i++) {
        std::string out = "";
        out += mode_string[i];
        left_win.write(out, false);
    }

    left_win.crop_to_next_line();
    left_win.crop_to_next_line();
    left_win.writeLineC(ownerS, false, BLUE_ON_BLACK);
    left_win.write(current_owner, false);
    if (selected_line == 1) {
        left_win.writeC(" ", false, BLACK_ON_BLUE);
    }
    left_win.crop_to_next_line();

    left_win.writeLineC(groupS, false, BLUE_ON_BLACK);
    left_win.write(current_group, false);
    if (selected_line == 2) {
        left_win.writeC(" ", false, BLACK_ON_BLUE);
    }
    left_win.crop_to_next_line();
}


void initialize_priority_mode(std::vector<std::string> *current_path,
                                std::string *selected) {
    current_path -> push_back(*selected);
    mode_string = get_file_permissions(join_path(*current_path));
    current_owner = get_file_owner(join_path(*current_path));
    log(current_owner);
    current_group = get_file_group(join_path(*current_path));
    log(current_group);
    current_path -> pop_back();
    mode_position = 1;


    priority_mode_draw();
}


void priority_mode_left_handler() {
    if (mode_position > 1) {
        mode_position--;
    }
    priority_mode_draw();
}


void priority_mode_right_handler() {
    if (mode_position + 1 < mode_string.size()) {
        mode_position++;
    }
    priority_mode_draw();
}


void priority_mode_updown_handler() {
    std::vector<char> modes = {'x', 'r', 'w'};
    int mode = mode_position % 3;

    if (mode_string[mode_position] == '-') {
        mode_string[mode_position] = modes[mode];
    } else {
        mode_string[mode_position] = '-';
    }
    priority_mode_draw();
}


void priority_mode_enter_handler(std::vector<std::string> *current_path,
                        std::vector<File> *previous_step_files,
                        std::vector<File> *current_step_files,
                        std::vector<File> *next_step_files,
                        std::string *selected) {
    if (previous_step_files -> size() > 0) {
        std::string selected_in_previous = current_path -> back();
        update_win(&left_win, join_path(*current_path),
                previous_step_files, selected_in_previous, false);
    } else {
        create_box(&left_win, false);
    }
    show_right_part(current_step_files, next_step_files, current_path,
                                                *selected);
    update_win(&main_win, join_path(*current_path),
            current_step_files, *selected, true);
}


void write_to_current(char ch) {
    if (ch == 127 || ch == 8) {
        if (current -> size() > 0) {
            current -> pop_back();
        }
    } else {
        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                || ch == '_' || (ch >= '0' && ch <= '9')) {
            current -> push_back(ch);
        }
    }

    priority_mode_draw();
}


void add_status_message(std::string message, int color) {
    command_win.writeC(message, false, color);
}


int main() {
    initscr();
    if (has_colors() == false) {
        endwin();
        std::cout << "Your terminal does not support colors." << std::endl;
        return 0;
    }
    initialize_gui_settings();
    initialize_main_windows(&address_win, &left_win,
            &main_win, &right_win, &command_win);

    std::vector<std::string> current_path = split_path(get_exec_path());
    std::vector<File> previous_step_files;
    std::vector<File> current_step_files;
    std::vector<File> next_step_files;
    std::string selected = ".";
    full_update(&current_path, &previous_step_files,
        &current_step_files, &next_step_files, &selected);

    while (42) {
        int ch = getch();
        if (selected_line > 0 && current_mode == PRIORI_MODE) {
            write_to_current(ch);
        }

        if (ch == LEFT_KEY) {
            if (current_mode == NORMAL_MODE) {
                left_handler(&current_path, &previous_step_files,
                    &current_step_files, &next_step_files, &selected);
            } else if (current_mode == PRIORI_MODE) {
                priority_mode_left_handler();
            }
        } else if (ch == DOWN_KEY) {
            if (current_mode == NORMAL_MODE) {
                down_handler(&current_path, &previous_step_files,
                    &current_step_files, &next_step_files, &selected);
            } else if (current_mode == PRIORI_MODE) {
                priority_mode_updown_handler();
            }
        } else if (ch == UP_KEY) {
            if (current_mode == NORMAL_MODE) {
                up_handler(&current_path, &previous_step_files,
                    &current_step_files, &next_step_files, &selected);
            } else if (current_mode == PRIORI_MODE) {
                priority_mode_updown_handler();
            }
        } else if (ch == RIGHT_KEY) {
            if (current_mode == NORMAL_MODE) {
                right_handler(&current_path, &previous_step_files,
                    &current_step_files, &next_step_files, &selected);
            } else if (current_mode == PRIORI_MODE) {
                priority_mode_right_handler();
            }
        } else if (ch == HELP_KEY) {

        } else if (current_mode == NORMAL_MODE && ch == PRIO_KEY) {
            current_mode = PRIORI_MODE;
            initialize_priority_mode(&current_path, &selected);
        } else if (ch == KEY_ENTER) {
            if (current_mode == NORMAL_MODE) {
            } else if (current_mode == PRIORI_MODE) {
                current_path.push_back(selected);
                bool result = change_file_priority(join_path(current_path),
                        mode_string, current_owner, current_group);

                current_path.pop_back();
                current_mode = NORMAL_MODE;
                priority_mode_enter_handler(&current_path, &previous_step_files,
                    &current_step_files, &next_step_files, &selected);
                if (result) {
                    add_status_message("SUCCESS", GREEN_ON_BLACK);
                } else {
                    add_status_message("ERROR", BLACK_ON_YELLOW);
                }
            }
        } else if (ch == KEY_ESC) {
            if (current_mode == PRIORI_MODE) {
                current_mode = NORMAL_MODE;
                priority_mode_enter_handler(&current_path, &previous_step_files,
                    &current_step_files, &next_step_files, &selected);
                refresh();
            }
        } else if (ch == KEY_UP) {
            selected_line = std::max(selected_line - 1, 0);
            priority_mode_draw();
        } else if (ch == KEY_DOWN) {
            selected_line = std::min(selected_line + 1, 2);
            priority_mode_draw();
        } else if (ch == EXIT_KEY) {
            endwin();
            return 0;
        }
        if (selected_line == 1) {
            current = &current_owner;
        } else if (selected_line == 2) {
            current = &current_group;
        }

        update_address_line(&address_win, &current_path, selected);
        refresh();
    }

    return 0;
}
