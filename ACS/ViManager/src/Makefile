all: clean main appgui fileutils link

main:
	-rm main.o
	g++ main.cpp -std=c++11 -c

appgui:
	-rm gui/gui.o
	g++ gui/gui.cpp -std=c++11 -c -o gui/gui.o

fileutils:
	-rm fileutil/fileutil.o
	g++ fileutil/fileutil.cpp -std=c++11 -c -o fileutil/fileutil.o

link:
	g++ main.o gui/gui.o fileutil/fileutil.o -std=c++11 -lncurses -o ../build/main

clean:
	-rm -rf ../build/*
