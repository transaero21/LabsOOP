#include "Number.h"

#include <cmath>

Number::Number(long long value) {
    bool sign = value < 0;
    if (sign) value *= -1;
    this->size = int(std::ceil(((value > 0 ? std::log2(value) : 0) + 1) / 8));
    this->octets = new Octet[this->size];
    for (int i = 1; value != 0; i++) {
        this->octets[this->size - i] = Octet(value % 0x100);
        value /= 0x100;
    }
    setSign(sign);
}

Number::Number(const std::string &value) {
    std::string s = value;
    bool sign = Number::parseNumeralString(s);

    int i = 0, add;
    while (!s.empty()) {
        if (i % 8 == 0) this->appendOctets();
        this->octets[0].setBit(i % 8, s[s.length() - 1] % 2);
        i++;

        add = 0;
        for (char &c: s) {
            char dgt = char((c - '0') / 2 + add + '0');
            add = (c % 2 != 0) * 5;
            c = dgt;
        }
        if (s[0] == '0') s.erase(0, 1);
    }

    if (i % 8 == 0) this->appendOctets();
    this->setSign(sign);
}

Number::Number(const Number &number) {
    if (this == &number) return;
    this->octets = new Octet[number.size];
    std::copy(number.octets, number.octets + number.size, this->octets);
    this->size = number.size;
}

void Number::swap(Number &number) {
    std::swap(this->size, number.size);
    std::swap(this->octets, number.octets);
}

Number::Number(Number &&number) noexcept {
    this->swap(number);
}

Number &Number::operator=(const Number &number) {
    if (this != &number) {
        this->octets = new Octet[number.size];
        std::copy(number.octets, number.octets + number.size, this->octets);
        this->size = number.size;
    }
    return *this;
}

Number &Number::operator=(Number &&number) noexcept {
    this->swap(number);
    return *this;
}

Number Number::getComplementCode() const {
    bool sign = this->getSign();
    Number number = *this;

    if (!sign)
        std::copy(this->octets, this->octets + this->size, number.octets);
    else {
        for (int i = int(number.size) - 1; i >= 0; i--) {
            number.octets[i] = ~Octet(this->octets[i]);
            if (number.size - 1 == i) number.octets[i].addOctet(Octet(1));
            if (number.octets[i].getValue() == 0) {
                if (i > 0) {
                    number.octets[i - 1].addOctet(Octet(1));
                }
            }
        }
        number.setSign(this->getSign());
    }

    return number;
}

Number Number::operator+(const Number &number) const {
    Number x = this->getComplementCode(), y = number.getComplementCode();
    Number result;

    result.size = std::max(x.size, y.size);
    result.octets = new Octet[result.size];

    int add = 0;
    for (int i = 1; i <= result.size; i++) {
        add = result.octets[result.size - i].addOctet(Octet(add));
        if (i <= x.size)
            add += result.octets[result.size - i].addOctet(x.octets[x.size - i]);
        else
            add += result.octets[result.size - i].addOctet(!x.getSign() ? Octet(0) : Octet(255));
        if (i <= y.size)
            add += result.octets[result.size - i].addOctet(y.octets[y.size - i]);
        else
            add += result.octets[result.size - i].addOctet(!y.getSign() ? Octet(0) : Octet(255));
    }

    if (x.getSign() == y.getSign() && x.getSign() != result.getSign()) {
        result.appendOctets();
        result.setSign(false);
        if (x.getSign())
            result.octets[0] = ~(result.octets[0]);
    }

    return result.getComplementCode();
}

Number Number::operator-(const Number &num) const {
    Number number = num;
    number.setSign(!number.getSign());
    return *this + number;
}

Number Number::operator-() const {
    Number number = Number(*this);
    number.setSign(!number.getSign());
    return number;
}

Number& Number::operator++() {
    *this = std::move(*this + Number(1));
    return *this;
}

Number Number::operator++(int) {
    Number number = *this;
    ++(*this);
    return number;
}

Number& Number::operator--() {
    *this = std::move(*this + Number(-1));
    return *this;
}

Number Number::operator--(int) {
    Number number = *this;
    --(*this);
    return number;
}

void Number::setSign(bool sign) {
    if (this->size == 0 || this->octets == nullptr)
        throw std::logic_error("Number is not initialized yet");
    this->octets[0].setBit(7, sign);
}

bool Number::getSign() const {
    if (this->size == 0 || this->octets == nullptr)
        throw std::logic_error("Number is not initialized yet");
    return this->octets[0].getBit(7);
}

std::string Number::toNumeral(int base) const {
    if (2 > base || base > 16)
        throw std::invalid_argument("Invalid number system");

    int i = 0, j = 6;
    while (j >= 0 && this->octets[0].getBit(j) != 1) --j;

    std::string s = (this->size > 1 || j > -1) ? "1" : "0";

    if (j > -1) j--;
    else { i++; j = 6; }

    for (; i < this->size; i++, j = 7) {
        for (; j >= 0; j--) {
            int add = this->octets[i].getBit(j);
            for (int k = (int) s.length() - 1; k >= 0; k--) {
                int dgt = fromChar(s[k]) * 2 + add;
                add = dgt / base;
                s[k] = toChar(dgt % base);
            }
            if (add > 0) s.insert(0, 1, char(add + '0'));
        }
    }

    if (this->getSign()) s.insert(0, 1, '-');

    return s;
}

Number::~Number() {
    delete[] this->octets;
}

bool Number::parseNumeralString(std::string &s) {
    bool sign = false;

    s.erase(s.find_last_not_of(' ') + 1);
    s.erase(0, s.find_first_not_of(' '));

    if (s.length() > 0) {
        sign = s[0] == '-';
        if (sign) s.erase(0, 1);
    }

    if (s.empty())
        throw std::invalid_argument("String is empty");

    for (char& c : s)
        if ('0' > c || c > '9')
            throw std::invalid_argument("String contains garbage");

    if (s.length() != 1 && s[0] == '0')
        throw std::invalid_argument("String is invalid numeral");

    if (s[0] == '0') sign = false;

    return sign;
}

void Number::appendOctets() {
    auto *tmp = new Octet[this->size + 1];
    std::copy(this->octets, this->octets + this->size, tmp + 1);
    delete[] this->octets;
    this->octets = tmp;
    this->size++;
}

int Number::fromChar(char c) {
    if ('0' <= c && c <= '9') {
        return c - '0';
    } else if ('A' <= c && c <='F') {
        return c - 'A' + 10;
    }
    return 0;
}

char Number::toChar(int i) {
    if (0 <= i && i <= 9) {
        return char(i + '0');
    } else if (10 <= i && i <= 15) {
        return char(i - 10 + 'A');
    }
    return '\0';
}

std::istream &operator>>(std::istream &is, Number &number) {
    std::string value;
    std::cin >> value;
    if (is.good()) {
        try {
            number = Number(value);
        } catch (const std::exception &e) {
            is.setstate(std::ios::failbit);
        }
    }
    return is;
}

std::ostream &operator<<(std::ostream &os, const Number &number) {
    os << number.toNumeral(10);
    return os;
}
