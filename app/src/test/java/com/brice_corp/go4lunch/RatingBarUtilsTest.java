package com.brice_corp.go4lunch;

import com.brice_corp.go4lunch.utils.RatingBarUtils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RatingBarUtilsTest {
    private static final String TAG = "ExampleUnitTest";
    //RatingBarUtils
    @Test
    public void CalculateRatingBarIsTrue() {
        Assert.assertEquals(RatingBarUtils.CalculateRatingBar(5), 3, 0.01);
        Assert.assertEquals(RatingBarUtils.CalculateRatingBar(4.55f), 2.73, 0.01);
        Assert.assertEquals(RatingBarUtils.CalculateRatingBar(1.2f), 0.72, 0.01);
        Assert.assertEquals(RatingBarUtils.CalculateRatingBar(2.64f), 1.58, 0.01);
    }

    //RatingBarUtils
    @Test
    public void CalculateRatingBarIsFalse() {
        Assert.assertNotEquals(RatingBarUtils.CalculateRatingBar(5), 4, 0.01);
        Assert.assertNotEquals(RatingBarUtils.CalculateRatingBar(4.55f), 3.53, 0.01);
        Assert.assertNotEquals(RatingBarUtils.CalculateRatingBar(1.2f), 4.7, 0.01);
        Assert.assertNotEquals(RatingBarUtils.CalculateRatingBar(2.64f), 2, 0.01);
    }
}