all: clean main

main:
	-rm main.o
	g++ main.cpp -std=c++11 -c

link:
	g++ main.o -std=c++11 -lncurses -o ../build/main

clean:
	-rm -rf ../build/*
