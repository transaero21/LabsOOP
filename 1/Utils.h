#ifndef UTILS_H
#define UTILS_H

#include <limits>

template<class T>
struct Array {
    T *data = nullptr;
    int size = 0;
    int rsize = 0;
};

template<class T>
void appendArray(Array<T> &array, T value) {
    if (array.rsize < array.size + 1) {
        T *arr = new T[array.rsize + 10];
        array.rsize += 10;
        std::copy(array.data, array.data + array.size, arr);
        delete[] array.data;
        array.data = arr;
    }
    array.data[array.size] = value;
    array.size += 1;
}

int getInt(int min = std::numeric_limits<int>::min());

#endif /* UTILS_H */
