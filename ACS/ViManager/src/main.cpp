#include <iostream>
#include <stack>
#include <vector>
#include <map>
#include <algorithm>

#include "ncurses.h"
#include "gui/gui.h"
#include "fileutil/fileutil.h"


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


size_t find(const std::vector<File> &files, const std::string &filename) {
    for (size_t i = 0; i < files.size(); i++) {
        if (files[i].filename == filename) {
            return i;
        }
    }

    return files.size();
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
    *selected = ".";
}


void update_win(WIN *win, std::vector<File> *files, std::string selected) {
    create_box(win, false);
    
    for (auto file: *files) {
        if (file.d_type == DT_DIR) {
            win -> writeLineC(file.filename, false, BLUE_ON_BLACK);
        } else {
            win -> writeLine(file.filename, false);
        }
    }

    if (selected != "") {
        int ind = find(*files, selected);
        int color = BLACK_ON_YELLOW;
        if ((*files)[ind].d_type == DT_DIR) {
            color = BLACK_ON_BLUE;
        }

        win -> set_line(ind);
        win -> writeC((*files)[ind].filename, false, color);
        for (int i = 0; i < 200; i++) {
            win -> writeC(" ", false, color);
        }
    }

    refresh();
}


void update_address_line(WIN *win, std::vector<std::string> *path,
                                                const std::string &selected) {
    create_box(win, false);
    win -> writeC(get_exec_user() + ":", false, GREEN_ON_BLACK);
    win -> writeC(join_path(*path), false, BLUE_ON_BLACK);
    win -> writeC("/", false, BLUE_ON_BLACK);
    win -> writeC(selected, false, WHITE_ON_BLACK);

    refresh();
}


int main() {
    initscr();
    if (has_colors() == false) {
        endwin();
        std::cout << "Your terminal does not support colors." << std::endl;
        return 0;
    }
    initialize_gui_settings();
    
    WIN address_win;
    WIN left_win;
    WIN main_win;
    WIN right_win;
    WIN command_win;

    initialize_main_windows(&address_win, &left_win,
            &main_win, &right_win, &command_win);

    /*
    address_win.writeC(get_exec_user() + ":", false, GREEN_ON_BLACK);
    address_win.writeC(get_exec_path(), false, BLUE_ON_BLACK);

    std::vector<std::string> current_state = split_path(get_exec_path());
    std::string selected_now = 0;

    update_win_for_dir(&main_win, join_path(current_state), 0);
    std::string temp = current_state.back();
    current_state.pop_back();
    update_win_for_dir(&left_win, join_path(current_state), 0);
    current_state.push_back(temp);
    */

    std::vector<std::string> current_path = split_path(get_exec_path());
    std::vector<File> previous_step_files;
    std::vector<File> current_step_files;
    std::vector<File> next_step_files;
    std::string selected = "";

    initialize_state(&current_path, &previous_step_files,
            &current_step_files, &next_step_files, &selected);
    update_win(&main_win, &current_step_files, selected);
    update_address_line(&address_win, &current_path, selected);

    while (42) {
        int ch = getch();

        if (ch == LEFT_KEY) {

        } else if (ch == DOWN_KEY) {
            int ind = find(current_step_files, selected);            
            if (ind + 1 < current_step_files.size()) {
                ind++;
            }

            selected = current_step_files[ind].filename;
            update_win(&main_win, &current_step_files, selected);
        } else if (ch == UP_KEY) {
            int ind = find(current_step_files, selected);            
            if (ind - 1 > -1) {
                ind--;
            }

            selected = current_step_files[ind].filename;
            update_win(&main_win, &current_step_files, selected);
        } else if (ch == RIGHT_KEY) {

        } else if (ch == HELP_KEY) {

        } else if (ch == EXIT_KEY) {
            endwin();
            return 0;
        }

        update_address_line(&address_win, &current_path, selected);
    }

    return 0;
}
