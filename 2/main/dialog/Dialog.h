#ifndef DIALOG_H
#define DIALOG_H

#include <Number.h>

void dialog();

struct MenuOption {
    const char *text = "";
    void (*fun)(Number &) = nullptr;
};

void setNumber(Number &num);
void addNumbers(Number &num);
void getNegative(Number &num);
void incrementNumber(Number &num);
void decrementNumber(Number &num);

static const MenuOption options[] = {
        {"Set number", setNumber},
        {"Add two numbers",  addNumbers},
        {"Get a negative number", getNegative},
        {"Number increment", incrementNumber},
        {"Number decrement", decrementNumber},
};
static const size_t count = sizeof(options) / sizeof(struct MenuOption);

#endif /* DIALOG_H */
