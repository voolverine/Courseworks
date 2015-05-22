all:
	g++ main.cpp -pthread -std=c++11 -c
	g++ checker.cpp -pthread -std=c++11 -c
	g++ parser.cpp -pthread -std=c++11 -c
	g++ main.o parser.o checker.o -pthread -std=c++11 -o ../build/main

main:
	-rm main.o
	g++ main.cpp -pthread -std=c++11 -c

parser:
	-rm parser.o
	g++ parser.cpp -std=c++11 -c

checker:
	-rm checker.o
	g++ checker.cpp -std=c++11 -c

link:
	g++ main.o parser.o checker.o -pthread -std=c++11 -o ../build/main

clear:
	-rm -rf ../build/*
