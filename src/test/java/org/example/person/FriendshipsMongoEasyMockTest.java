package org.example.person;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

import java.rmi.UnknownHostException;
import java.util.Arrays;
import java.util.List;

@ExtendWith(EasyMockExtension.class)
public class FriendshipsMongoEasyMockTest {

    @TestSubject
    FriendshipsMongo friendships = new FriendshipsMongo();

    //A nice mock expects recorded calls in any order and returning null for other calls
    @Mock(type = MockType.NICE)
    FriendsCollection friends;

    @Test
    public void mockingWorksAsExpected(){
        Person joe = new Person("Joe");
        //Zapisanie zachowania - co sie ma stac
        expect(friends.findByName("Joe")).andReturn(joe);
        //Odpalenie obiektu do sprawdzenia zachowania
        replay(friends);
        assertThat(friends.findByName("Joe")).isEqualTo(joe);
    }

    @Test
    public void alexDoesNotHaveFriends(){
        assertThat(friendships.getFriendsList("Alex")).isEmpty();
    }

    @Test
    public void joeHas5Friends(){
        List<String> expected = Arrays.asList(new String[]{"Karol","Dawid","Maciej","Tomek","Adam"});
        Person joe = createMock(Person.class);
        expect(friends.findByName("Joe")).andReturn(joe);
        expect(joe.getFriends()).andReturn(expected);
        replay(friends);
        replay(joe);
        assertThat(friendships.getFriendsList("Joe")).hasSize(5).containsOnly("Karol","Dawid","Maciej","Tomek","Adam");
    }

    @Test
    public void BartHasNoFriends(){
        List<String> expected = Arrays.asList(new String[]{});
        Person bart = createMock(Person.class);
        expect(friends.findByName("Bart")).andReturn(bart);
        expect(bart.getFriends()).andReturn(expected);
        replay(friends);
        replay(bart);
        assertThat(friendships.getFriendsList("Bart")).isEmpty();
    }

    @Test
    public void areFriends() {
        List<String> joesFriends = Arrays.asList(new String[]{"Bart"});
        List<String> bartsFriends = Arrays.asList(new String[]{"Joe"});
        Person joe = createMock(Person.class);
        Person bart = createMock(Person.class);
        expect(friends.findByName("Bart")).andReturn(bart);
        expect(friends.findByName("Joe")).andReturn(joe);
        expect(bart.getFriends()).andReturn(bartsFriends);
        expect(joe.getFriends()).andReturn(joesFriends);
        replay(friends);
        replay(joe, bart);
        assertThat(friendships.areFriends("Bart", "Joe")).isTrue();
    }

    @Test
    public void areNoFriends() {
        List<String> noFriends = Arrays.asList(new String[]{});
        Person joe = createMock(Person.class);
        Person bart = createMock(Person.class);
        expect(friends.findByName("Bart")).andReturn(bart);
        expect(friends.findByName("Joe")).andReturn(joe);
        expect(bart.getFriends()).andReturn(noFriends);
        expect(joe.getFriends()).andReturn(noFriends);
        replay(friends);
        replay(joe, bart);
        assertThat(friendships.areFriends("Bart", "Joe")).isFalse();
    }

    @Test
    public void notOnAList() {
        assertThat(friendships.getFriendsList("Mark")).isEmpty();
    }
}