#include <stdexcept>

#include "Octet.h"

Octet::Octet(const int (&octet)[8]) {
    for (int i = 0; i < 8; i++)
        this->value += bool(octet[7 - i]) << i;
}

Octet::Octet(uint8_t octet) {
    this->value = octet;
}

Octet Octet::operator~() const {
    return Octet(~this->value);
}

bool Octet::getBit(int pos) const {
    if (0 > pos || pos > 7)
        throw std::out_of_range("Octet index out of bound");
    return this->value >> pos & 1;
}

void Octet::setBit(int pos, int bit) {
    if (0 > pos || pos > 7)
        throw std::out_of_range("Octet index out of bound");
    this->value = this->value & ~(1 << pos) | (!!bit << pos);
}

uint8_t Octet::getComplement() const {
    return 0x100 - this->value;
}

bool Octet::addOctet(const Octet &octet) {
    uint8_t sum = this->value + octet.value;
    bool bit = sum < this->value;
    this->value = sum;
    return bit;
}

std::ostream &operator<<(std::ostream &os, const Octet &octet) {
    for (int i = 7; i > -1; i--) {
        os << octet.getBit(i);
    }
    return os;
}

std::istream &operator>>(std::istream &is, Octet &octet) {
    std::string s;
    std::cin >> s;
    if (is.good()) {
        if (s.length() != 8) {
            is.setstate(std::ios::failbit);
            return is;
        }

        uint8_t value = 0;
        for (int i = 0; i < 8; i++)
            value += bool(s[7 - i]) << i;
        octet.value = value;
    }
    return is;
}
