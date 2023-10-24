#include <gtest/gtest.h>

#include <Octet.h>

namespace {
    TEST(OctetTest, CreateWithArray) {
        EXPECT_EQ(Octet({0, 1, 0, 1, 0, 1, 0, 1}).getValue(), 0b01010101);
        EXPECT_EQ(Octet({0, 0, 0, 0, 0, 0, 0, 0}).getValue(), 0b00000000);
        EXPECT_EQ(Octet({1, 1, 1, 1, 1, 1, 1, 1}).getValue(), 0b11111111);
        EXPECT_EQ(Octet({0, 1, 2, 3, 4, -1, -2, -3}).getValue(), 0b01111111);
    }

    TEST(OctetTest, GetBit) {
        EXPECT_EQ(Octet({0, 1, 0, 1, 0, 0, 1, 0}).getBit(3), 0);
        EXPECT_EQ(Octet({1, 0, 0, 0, 1, 0, 1, 1}).getBit(6), 0);
        EXPECT_EQ(Octet({1, 1, 0, 2, 0, 4, 1, 1}).getBit(4), 1);
        EXPECT_THROW(Octet({1, 1, 0, 1, 0, 0, 1, 0}).getBit(8), std::out_of_range);
        EXPECT_THROW(Octet({0, 0, 0, 0, 1, 0, 1, 1}).getBit(-1), std::out_of_range);
    }

    ::testing::AssertionResult test_SetBit(const int (&values)[8], int pos, int bit, uint8_t exp) {
        auto octet = Octet(values);
        octet.setBit(pos, bit);
        if (octet.getValue() != exp)
            return ::testing::AssertionFailure() << "Incorrect bit setting result";
        return ::testing::AssertionSuccess();
    }

    TEST(OctetTest, SetBit) {
        EXPECT_TRUE(test_SetBit({0, 1, 0, 1, 0, 0, 1, 0}, 0, 1, 0b01010011));
        EXPECT_TRUE(test_SetBit({1, 1, 1, 0, 0, 1, 1, 1}, 4, 0, 0b11100111));
        EXPECT_TRUE(test_SetBit({1, 0, 1, 1, 0, 0, 1, 1}, 7, 0, 0b00110011));
        EXPECT_THROW(test_SetBit({0, 1, 0, 1, 0, 0, 1, 0}, 8, 0, 0), std::out_of_range);
        EXPECT_THROW(test_SetBit({1, 0, 0, 1, 0, 0, 0, 1}, -1, 1, 0), std::out_of_range);
    }

    TEST(OctetTest, LogicNot) {
        EXPECT_EQ((~Octet({1, 1, 0, 1, 1, 0, 0, 0})).getValue(), 0b00100111);
        EXPECT_EQ((~Octet({1, 0, 0, 0, 0, 1, 1, 1})).getValue(), 0b01111000);
        EXPECT_EQ((~Octet({0, 0, 0, 0, 0, 0, 0, 0})).getValue(), 0b11111111);
        EXPECT_EQ((~Octet({1, 0, 1, 0, 0, 1, 0, 1})).getValue(), 0b01011010);
    }

    TEST(OctetTest, GetComplement) {
        EXPECT_EQ(Octet(44).getComplement(), 212);
        EXPECT_EQ(Octet(123).getComplement(), 133);
        EXPECT_EQ(Octet(36).getComplement(), 220);
        EXPECT_EQ(Octet(252).getComplement(), 4);
    }

    ::testing::AssertionResult test_AddOctet(int value, int arg, int sum, int bit) {
        auto octet = Octet(value);
        if (octet.addOctet(Octet(arg)) != bit)
            return ::testing::AssertionFailure() << "Invalid overflow bit";
        if (octet.getValue() != sum)
            return ::testing::AssertionFailure() << "Invalid sum result";
        return ::testing::AssertionSuccess();
    }

    TEST(OctetTest, AddOctet) {
        EXPECT_TRUE(test_AddOctet(44, 123, 167, 0));
        EXPECT_TRUE(test_AddOctet(230, 27, 1, 1));
        EXPECT_TRUE(test_AddOctet(179, 21, 200, 0));
        EXPECT_TRUE(test_AddOctet(210, 189, 143, 1));
    }
}
