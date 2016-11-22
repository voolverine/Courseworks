#define GREEN_ON_BLACK  1
#define BLACK_ON_GREEN  2
#define BLUE_ON_BLACK   3
#define BLACK_ON_BLUE   4
#define YELLOW_ON_BLUE  5
#define BLACK_ON_YELLOW 6
#define WHITE_ON_BLACK  7



#include <string>

#define LEFT_KEY 'h'
#define DOWN_KEY 'j'
#define UP_KEY 'k'
#define RIGHT_KEY 'l'
#define HELP_KEY KEY_F(1)
#define EXIT_KEY KEY_F(10)
#define PRIO_KEY KEY_F(8)
#define KEY_ENTER 10
#define KEY_ESC 27

#define NORMAL_MODE 1
#define PRIORI_MODE 2
#define INSERT_MODE 3

struct WIN_BORDER {
    chtype  ls, rs, ts, bs,
        tl, tr, bl, br;
};


struct WIN {

    int startx, starty;
    int height, width;
    WIN_BORDER border;

    int current_position_x = -1;
    int current_position_y = -1;

    bool can_add_one_char_in_line() {
        return current_position_x < startx + width - 1;
    }

    bool can_add_one_char_in_window() {
        if (current_position_y < starty + height - 1) {
            return true;
        }

        if (current_position_y == starty + height - 1 &&
                can_add_one_char_in_line()) {
            return true;
        }

        return false;
    }


    void set_line(int line) {
        current_position_x = startx + 1;
        current_position_y = starty + line;
    }

    void crop_to_next_line() {
        current_position_x = startx + 1;
        current_position_y++;
    }


    void write(const std::string &line, const bool crop) {
        if (current_position_x == -1 ||
                current_position_y == -1) {
            current_position_x = startx + 1;
            current_position_y = starty;
        }

        if (!can_add_one_char_in_window()) {
            return;
        }

        for (size_t i = 0; i < line.size(); i++) {
            if (!can_add_one_char_in_line()) {
                if (crop) {
                    crop_to_next_line();
                    mvaddch(current_position_y, current_position_x++, line[i]);
                    continue;
                }
            } else {
                mvaddch(current_position_y, current_position_x++, line[i]);
            }
        }
    }


    void writeLine(const std::string &line, const bool crop) {
        write(line, crop);
        crop_to_next_line();
    }


    void writeC(const std::string &line, const bool crop, int color_pair) {
        attron(COLOR_PAIR(color_pair));
        write(line, crop);
        attroff(COLOR_PAIR(color_pair));
    }


    void writeLineC(const std::string &line, const bool crop, int color_pair) {
        attron(COLOR_PAIR(color_pair));
        writeLine(line, crop);
        attroff(COLOR_PAIR(color_pair));
    }

    void writeRight(const std::string &line) {
        current_position_x =
            std::max((int)startx + width  - (int)line.size(), 0);

        for (size_t i = 0; i < line.size(); i++) {
            mvaddch(current_position_y, current_position_x++, line[i]);
        }

        current_position_x = startx + 1;
    }

    void writeRightC(const std::string &line, int color_pair) {
        attron(COLOR_PAIR(color_pair));
        writeRight(line);
        attroff(COLOR_PAIR(color_pair));
    }
};


void initialize_gui_settings();
void init_win_params(WIN *p_win, int height, int width, int startx, int starty);
void create_box(WIN *p_win, bool flag);
