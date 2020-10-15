package com.brice_corp.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.brice_corp.go4lunch.model.User;
import com.brice_corp.go4lunch.repository.FirestoreUserRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by <NIATEL Brice> on <13/10/2020>.
 */
public class FirestoreRepoTest {
    private final String IDRESTAURANT = "xxx";
    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private FirestoreUserRepository mFtUserRepository = Mockito.mock(FirestoreUserRepository.class);

    private MutableLiveData<Boolean> mGetTheLikeRestaurantLiveData;

    private MutableLiveData<String> mGetTheEatTodayLiveData;

    @Before
    public void setUp() {
        //getTheLikeRestaurantLiveDataFalse() + getTheLikeRestaurantLiveDataTrue()
        mGetTheLikeRestaurantLiveData = new MutableLiveData<>();
        Mockito.doReturn(mGetTheLikeRestaurantLiveData).when(mFtUserRepository).getTheLikeRestaurant(IDRESTAURANT);

        //getTheEatTodayLiveDataNothing() + getTheEatTodayLiveData()
        mGetTheEatTodayLiveData = new MutableLiveData<>();
        Mockito.doReturn((mGetTheEatTodayLiveData)).when(mFtUserRepository).getTheEatToday();

        //getUser()
        Mockito.doReturn((new User("Toto", "toto@gmail.com", "https://user.oc-static.com/users/avatars/15853453750743_IMG_20200311_190120.jpg")
        )).when(mFtUserRepository).getUser();
    }

    @Test
    public void getTheLikeRestaurantLiveDataFalse() throws InterruptedException {
        //Given
        boolean resultBoolean = false;
        mGetTheLikeRestaurantLiveData.setValue(false);

        //When
        Boolean result = LiveDataUtils.getOrAwaitValue(mFtUserRepository.getTheLikeRestaurant(IDRESTAURANT));

        //Test
        Assert.assertNotNull(result);
        Assert.assertEquals(result, resultBoolean);
    }

    @Test
    public void getTheLikeRestaurantLiveDataTrue() throws InterruptedException {
        //Given
        boolean resultBoolean = true;
        mGetTheLikeRestaurantLiveData.setValue(true);

        //When
        Boolean result = LiveDataUtils.getOrAwaitValue(mFtUserRepository.getTheLikeRestaurant(IDRESTAURANT));

        //Test
        Assert.assertNotNull(result);
        Assert.assertEquals(result, resultBoolean);
    }

    @Test
    public void getTheEatTodayLiveDataNothing() throws InterruptedException {
        //Given
        String resultString = "";
        mGetTheEatTodayLiveData.setValue("");

        //When
        String result = LiveDataUtils.getOrAwaitValue(mFtUserRepository.getTheEatToday());

        //Test
        Assert.assertNotNull(result);
        Assert.assertEquals(result, resultString);
    }

    @Test
    public void getTheEatTodayLiveData() throws InterruptedException {
        //Given
        mGetTheEatTodayLiveData.setValue("xxx");

        //When
        String result = LiveDataUtils.getOrAwaitValue(mFtUserRepository.getTheEatToday());

        //Test
        Assert.assertNotNull(result);
        Assert.assertEquals(result, IDRESTAURANT);
    }

    @Test
    public void getUser() {
        //Test
        Assert.assertEquals(mFtUserRepository.getUser().getName(), "Toto");
        Assert.assertEquals(mFtUserRepository.getUser().getEmail(), "toto@gmail.com");
        Assert.assertEquals(mFtUserRepository.getUser().getImage(), "https://user.oc-static.com/users/avatars/15853453750743_IMG_20200311_190120.jpg");
    }
}
