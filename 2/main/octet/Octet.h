#pragma ide diagnostic ignored "modernize-use-nodiscard"

#ifndef OCTET_H
#define OCTET_H

#include <iostream>
#include <cstdint>

/**
 * Class of an octet.
 * Octet is a unit that consists of 8 bits (1 byte).
 */
class Octet {
private:
    /**
     * Property that contains an octet (8 bits).
     * The bits of an octet are indexed from right to left starting at 0.
     */
    uint8_t value = 0;

public:
    /**
     * Default constructor.
     * Initialize octet with zero value.
     */
    Octet() = default;

    /**
     * Initializes an octet by array bits.
     * Array indexes have the same logic.
     * @see Octet#value.
     * @param octet 8 bit array.
     */
    explicit Octet(const int (&octet)[8]);

    /**
     * Initializes an octet with specified value.
     * @param octet value of octet.
     */
    explicit Octet(uint8_t octet);

    /**
     * Getter for Octet#value.
     * @return value of octet.
     */
    uint8_t getValue() const { return this->value; }

    /**
     * Returns the bit at the specified index in an octet.
     * @see Octet#value.
     * @param pos index of bit.
     * @return value of bit.
     */
    bool getBit(int pos) const;

    /**
     * Sets a bit at the specified index in an octet.
     * @see Octet#value.
     * @param pos index of bit.
     * @param bit value of bit.
     */
    void setBit(int pos, int bit);

    /**
     * Returns the complement of an octet.
     * @return octet's complement.
     */
    uint8_t getComplement() const;

    /**
     * Adds an octet.
     * @param octet second octet sum argument.
     * @return overflow bit.
     */
    bool addOctet(const Octet &octet);

    Octet operator~() const;

    friend std::istream &operator>>(std::istream &is, Octet &octet);
    friend std::ostream &operator<<(std::ostream &os, const Octet &octet);
};

#endif /* OCTET_H */
