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
    command_win.write(permissions, false);
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
        create_box(&right_win, false);
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
        full_update(current_path, previous_step_files,
                current_step_files, next_step_files, selected);
    }
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

        if (ch == LEFT_KEY) {
            left_handler(&current_path, &previous_step_files,
                &current_step_files, &next_step_files, &selected);
        } else if (ch == DOWN_KEY) {
            down_handler(&current_path, &previous_step_files,
                &current_step_files, &next_step_files, &selected);
        } else if (ch == UP_KEY) {
            up_handler(&current_path, &previous_step_files,
                &current_step_files, &next_step_files, &selected);
        } else if (ch == RIGHT_KEY) {
            right_handler(&current_path, &previous_step_files,
                &current_step_files, &next_step_files, &selected);
        } else if (ch == HELP_KEY) {

        } else if (ch == EXIT_KEY) {
            endwin();
            return 0;
        }

        update_address_line(&address_win, &current_path, selected);
        refresh();
    }

    return 0;
}
