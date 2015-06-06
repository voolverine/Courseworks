all: clean main

main:
	g++ main.cpp ../../hashlib/hash.o -std=c++11 -o ../build/swiki

clean:
	find ../build/ -name "swiki" -exec rm -r "{}" \;
