#include "ncurses.h"

#include "gui.h"

void init_win_params(WIN *p_win, int height, int width,
                    int startx, int starty)
{
    p_win->height = height;
    p_win->width = width;
    p_win->starty = starty;
    p_win->startx = startx;

    p_win->border.ls = '|';
    p_win->border.rs = '|';
    p_win->border.ts = '-';
    p_win->border.bs = '-';
    p_win->border.tl = '+';
    p_win->border.tr = '+';
    p_win->border.bl = '+';
    p_win->border.br = '+';

}


void create_box(WIN *p_win, bool flag) {
    int x, y, w, h;
    x = p_win->startx;
    y = p_win->starty;
    w = p_win->width;
    h = p_win->height;
    p_win -> current_position_x = p_win -> startx + 1;
    p_win -> current_position_y = p_win -> starty;

    if (flag) {
        mvaddch(y - 1, x - 1, p_win->border.tl);
        mvaddch(y - 1, x + w, p_win->border.tr);
        mvaddch(y + h, x - 1, p_win->border.bl);
        mvaddch(y + h, x + w, p_win->border.br);
        mvhline(y - 1, x + 1, p_win->border.ts, w - 1);
        mvhline(y + h, x + 1, p_win->border.bs, w - 1);
        mvvline(y + 1, x - 1, p_win->border.ls, h - 1);
        mvvline(y + 1, x + w, p_win->border.rs, h - 1);

    } else {
        for(int j = y; j < y + h; j++) {
            for(int i = x; i < x + w; i++) {
                mvaddch(j, i, ' ');
            }
        }
    }

    refresh();
}

void initialize_gui_settings() {
    start_color();
    curs_set(0);
    noecho();
    keypad(stdscr, true);
    attron(A_BOLD);


    init_color(COLOR_BLACK, 148, 148, 148);
    init_color(COLOR_GREEN, 612, 720, 220);
    init_color(COLOR_BLUE, 612, 860, 952);
    init_color(COLOR_YELLOW, 912, 960, 896);
    init_color(COLOR_WHITE, 1000, 1000, 1000);
    
    init_pair(GREEN_ON_BLACK, COLOR_GREEN, COLOR_BLACK);
    init_pair(BLACK_ON_GREEN, COLOR_BLACK, COLOR_GREEN);
    init_pair(BLUE_ON_BLACK, COLOR_BLUE, COLOR_BLACK);
    init_pair(BLACK_ON_BLUE, COLOR_BLACK, COLOR_BLUE);
    init_pair(YELLOW_ON_BLUE, COLOR_YELLOW, COLOR_BLUE);
    init_pair(BLACK_ON_YELLOW, COLOR_BLACK, COLOR_YELLOW);
    init_pair(WHITE_ON_BLACK, COLOR_WHITE, COLOR_BLACK);
}
