#include <gtest/gtest.h>

#include <Number.h>

namespace {
    ::testing::AssertionResult test_NumberCorrect(const Number &number,
                                                  const Octet (&octets)[],
                                                  size_t size) {
        if (number.getSize() != size)
            return ::testing::AssertionFailure() << "Size of numbers is different";
        for (int i = 0; i < size; i++)
            if (number.getOctets()[i].getValue() != octets[i].getValue())
                return ::testing::AssertionFailure() << "Numbers are different at " << i << " octet";
        return ::testing::AssertionSuccess();
    }

    ::testing::AssertionResult test_NumberEquals(const Number &arg1, const Number &arg2) {
        if (arg1.getSize() != arg2.getSize())
            return ::testing::AssertionFailure() << "Size of numbers is different";
        for (int i = 0; i < arg1.getSize(); i++)
            if (arg1.getOctets()[i].getValue() != arg2.getOctets()[i].getValue())
                return ::testing::AssertionFailure() << "Numbers are different at " << i << " octet";
        return ::testing::AssertionSuccess();
    }

    TEST(NumberTest, CreateWithIntegerTest) {
        EXPECT_TRUE(test_NumberCorrect(Number(17820386),
                                       {Octet({0, 0, 0, 0, 0, 0, 0, 1}), Octet({0, 0, 0, 0, 1, 1, 1, 1}),
                                        Octet({1, 1, 1, 0, 1, 0, 1, 0}), Octet({1, 1, 1, 0, 0, 0, 1, 0})},
                                       4));
        EXPECT_TRUE(test_NumberCorrect(Number(-799149526),
                                       {Octet({1, 0, 1, 0, 1, 1, 1, 1}), Octet({1, 0, 1, 0, 0, 0, 1, 0}),
                                        Octet({0, 0, 0, 0, 1, 1, 0, 1}), Octet({1, 1, 0, 1, 0, 1, 1, 0})},
                                       4));
        EXPECT_TRUE(test_NumberCorrect(Number(0), {Octet({0, 0, 0, 0, 0, 0, 0, 0})}, 1));
    }

    TEST(NumberTest, CreateWithStringTest) {
        EXPECT_TRUE(test_NumberCorrect(Number("-536794"),
                                       {Octet({1, 0, 0, 0, 1, 0, 0, 0}), Octet({0, 0, 1, 1, 0, 0, 0, 0}),
                                        Octet({1, 1, 0, 1, 1, 0, 1, 0})},
                                       3));
        EXPECT_TRUE(test_NumberCorrect(Number("1231231"),
                                       {Octet({0, 0, 0, 1, 0, 0, 1, 0}), Octet({1, 1, 0, 0, 1, 0, 0, 1}),
                                        Octet({0, 1, 1, 1, 1, 1, 1, 1})},
                                       3));
        EXPECT_TRUE(test_NumberCorrect(Number("   917   "),
                                       {Octet({0, 0, 0, 0, 0, 0, 1, 1}), Octet({1, 0, 0, 1, 0, 1, 0, 1}),},
                                       2));
        EXPECT_TRUE(test_NumberCorrect(Number("-0"), {Octet({0, 0, 0, 0, 0, 0, 0, 0})}, 1));
        EXPECT_THROW(Number(""), std::invalid_argument);
        EXPECT_THROW(Number("-09352"), std::invalid_argument);
        EXPECT_THROW(Number("Lorem Ipsum"), std::invalid_argument);
    }

    TEST(NumberTest, GetSignTest) {
        EXPECT_EQ(Number("5423679").getSign(), false);
        EXPECT_EQ(Number("-129381").getSign(), true);
    }

    TEST(NumberTest, ToNumeralTest) {
        EXPECT_EQ(Number("-1283238").toNumeral(10), "-1283238");
        EXPECT_EQ(Number("4652723").toNumeral(16), "46FEB3");
        EXPECT_EQ(Number("-764").toNumeral(3), "-1001022");
        EXPECT_EQ(Number("493723").toNumeral(12), "1B9877");
        EXPECT_EQ(Number("255").toNumeral(2), "11111111");
        EXPECT_THROW(Number("1").toNumeral(0), std::invalid_argument);
    }

    TEST(NumberTest, SumNumbersTest) {
        EXPECT_TRUE(test_NumberEquals(Number(2452) + Number(953), Number(3405)));
        EXPECT_TRUE(test_NumberEquals(Number(-972) + Number(53), Number(-919)));
    }

    TEST(NumberTest, MinusNumberTest) {
        EXPECT_TRUE(test_NumberEquals(-Number(2736), Number(-2736)));
        EXPECT_TRUE(test_NumberEquals(-Number(-3843), Number(3843)));
    }

    ::testing::AssertionResult test_BeforeIncrement(const Number &number) {
        Number num = number, old = num, result = ++num;
        if (test_NumberEquals(old + Number(1), num) && test_NumberEquals(result, num))
            return ::testing::AssertionSuccess();
        return ::testing::AssertionFailure() << "Invalid result";
    }

    TEST(NumberTest, BeforeIncrement) {
        EXPECT_TRUE(test_BeforeIncrement(Number(23)));
        EXPECT_TRUE(test_BeforeIncrement(Number(-64)));
    }

    ::testing::AssertionResult test_AfterIncrement(const Number &number) {
        Number num = number, old = num, result = num++;
        if (test_NumberEquals(old, result) && test_NumberEquals(old + Number(1), num))
            return ::testing::AssertionSuccess();
        return ::testing::AssertionFailure() << "Invalid result";
    }

    TEST(NumberTest, AfterIncrement) {
        EXPECT_TRUE(test_AfterIncrement(Number(2321)));
        EXPECT_TRUE(test_AfterIncrement(Number(-918)));
    }

    ::testing::AssertionResult test_BeforeDecrement(const Number &number) {
        Number num = number, old = num, result = --num;
        if (test_NumberEquals(old - Number(1), num) && test_NumberEquals(result, num))
            return ::testing::AssertionSuccess();
        return ::testing::AssertionFailure() << "Invalid result";
    }

    TEST(NumberTest, BeforeDecrement) {
        EXPECT_TRUE(test_BeforeDecrement(Number(354)));
        EXPECT_TRUE(test_BeforeDecrement(Number(-53)));
    }

    ::testing::AssertionResult test_AfterDecrement(const Number &number) {
        Number num = number, old = num, result = num--;
        if (test_NumberEquals(old, result) && test_NumberEquals(old - Number(1), num))
            return ::testing::AssertionSuccess();
        return ::testing::AssertionFailure() << "Invalid result";
    }

    TEST(NumberTest, AfterDecrement) {
        EXPECT_TRUE(test_AfterDecrement(Number(231)));
        EXPECT_TRUE(test_AfterDecrement(Number(-1128)));
    }
}
