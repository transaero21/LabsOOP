#pragma ide diagnostic ignored "modernize-use-nodiscard"

#ifndef NUMBER_H
#define NUMBER_H

#include <Octet.h>

/**
 * A class representing a large numeric value that cannot be
 * obtained with basic data types.
 */
class Number {
private:
    /** Number of octets. */
    size_t size = 0;

    /**
     * Array of octets that represent number.
     * The first bit (8 bit of 1st value) represent the sign of value.
     */
    Octet *octets = nullptr;

    /**
     * Set a sign of number.
     * @throws std::logic_error if number is not initialized yet.
     * @param sign a bit of a sign.
     */
    void setSign(bool sign);

    /**
     * Prepare string to convert string to class.
     * @throws std::invalid_argument if string is invalid numeral.
     * @return sign.
     */
    static bool parseNumeralString(std::string &s);

    /**
     * Adds an empty message before the others in Number#octets.
     */
    void appendOctets();

    /**
     * Returns the corresponding number from a symbol
     * for different number systems.
     * @param c symbol.
     * @return number or 0 if number system is invalid or too large.
     */
    static int fromChar(char c);

    /**
     * Returns the corresponding symbol from a number
     * for different number systems.
     * @param i number.
     * @return symbol or 0 if number system is invalid or too large.
     */
    static char toChar(int i);

    /**
     * A wrapper for move constructor and assignment.
     * @param number where to move.
     */
    void swap(Number &number);

    /** Default constructor */
    Number() = default;

public:
    /**
     * Initialize number with integer.
     * @param value integer.
     */
    explicit Number(long long value);

    /**
     * Initialize number with string representation.
     * @param s string representing number.
     */
    explicit Number(const std::string& s);

    /** Class destructor. */
    ~Number();

    Number(const Number &number);
    Number(Number &&number) noexcept;

    /**
     * Returns a number in two's complement.
     * @return complement.
     */
    Number getComplementCode() const;

    /**
     * Get a sign of number.
     * @return a bit of a sign.
     */
    bool getSign() const;

    /**
     * Converts a number to a given number system as a string.
     * @throws std::invalid_argument If the number system is invalid or too large.
     * @param base a base of numeral system.
     * @return number as a string.
     */
    std::string toNumeral(int base) const;

    Octet *getOctets() const { return this->octets; }
    size_t getSize() const { return this->size; }

    Number &operator=(const Number &number);
    Number &operator=(Number &&number) noexcept;
    Number operator+(const Number &number) const;
    Number operator-() const;
    Number operator-(const Number &number) const;
    Number &operator++();
    Number operator++(int);
    Number &operator--();
    Number operator--(int);

    friend std::istream &operator>>(std::istream &is, Number &number);
    friend std::ostream &operator<<(std::ostream &os, const Number &number);
};

#endif /* NUMBER_H */
