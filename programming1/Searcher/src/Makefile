all: clean main
	mv swiki ../../swiki

main:
	g++ main.cpp ../../hashlib/hash.o -std=c++11 -o ./swiki

clean:
	find . -name "swiki" -exec rm -r "{}" \;
