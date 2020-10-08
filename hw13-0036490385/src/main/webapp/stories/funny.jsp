<%@ page import="java.util.Random" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%!
private String getRandomColor() {
	String[] colors = new String[] {"blue","black","pink","darkblue","brown","darkmagenta","gold","yellow"};
	Random rnd = new Random();
	return colors[rnd.nextInt(colors.length)];

}


%>



<html>
<body style="color:<%  out.print(getRandomColor());%>;   background-color: <%out.print(session.getAttribute("pickedBgCol"));%>;     ">
<h1>A funny story</h1>

<p>Once upon a time there was a young girl named Sherry.  Sherry was 19 years old and lived in Rochester, New York.  While walking home from school one day,
 a pink German Shepard jumped out from behind a bush and tackled Sherry to the ground.  But just when she was about to let out a scream for help, 
 Sherry realized that the pink German Shepard was only licking her face, not trying to bite it off.  At that moment, Sherry decided to keep the pink German Shepard
  as a pet.  And on the way home she decided to name her pet pink German Shepard ''Bob.''  </p>

<p>When Sherry and her new pet finally got home, guess who was standing on the front porch?  That's right, it was Sherry's mother, Dorothy.  And boy was she
 surprised to see a pink German Shepard following Sherry into the yard!  ''What in world is that?'' shouted Dorothy.  ''It's a pink German Shepard,'' answered Sherry.
   ''Dah, I can see that, Sherry, but what on earth is it doing here?'' said Dorothy.  ''It's my new pet!'' answered Sherry.  ''Oh you think so do you?'' remarked Dorothy.
     ''I wouldn't get your hopes up. You know how your father hates pink German Shepards.  But, well, I suppose you can keep him until your father comes home.'' 
      And with that Sherry grabbed Bob by the scruff of the neck and led her new pet into the house--even though she knew her father was probably going to dissaprove.  </p>

<p>Once in the house, Sherry and Bob played and played, that is until Sherry's favorite television show, ''Faulty Towers,'' started.  At that point Sherry
 forgot all about Bob having an unsupervised run of the house.  That is until half way through ''Faulty Towers,'' when Sherry was brought back to reality
  when she heard her father shout, ''stupid!!  Sherry! Get your bum in the study room...NOW!!''  With that Sherry rushed into the study room to see what all
   the fuss was about.  When she entered the study room, there stood her father, Jeremy, pointing toward the stool.  ''Will someone please explain that?'' asked 
   her father.  Then, as Sherry followed her father's finger to where it was pointing, she instantly knew what her father was so upset about.  There, smack dab
    in the middle of the stool, was the biggest pile of German Shepard doo-doo she had ever seen!  ''I don't EVEN want to know how that got there,'' said Jeremy.
      ''But you had better get it cleaned up now!  And you had better get rid of whatever it is that could have done such a thing!''  Well, knowing her father as
       well as she did, Sherry knew there was no sense even asking her father if she could keep Bob for a pet.  </p>

<p>So without hesitation, Sherry set out to find where Bob was hiding.  After a few minutes of looking, Sherry discovered Bob crouched beneath the table that 
Sherry did her Badminton on.  ''Come on, Bob, it's time to find you a new home.  And hey, don't look at me that way, I'm not the one who did the dirty
 deed on the stool!'' scolded Sherry.  ''Thanks to you I'll never get to have my own pet German Shepard!!  And with that Sherry led Bob out of the house and 
 down to the local B-Mart.  They had a pet section and Sherry knew the owner would find Bob a good home.  </p>

<p>So after saying good-bye to Bob, and thanking the owner of B-Mart, Sherry walked backed home and attempted to dround her sorrows by slamming down a half dozen Diet Cokes.  
But Sherry's pitty party came to an abrupt end when her father reminded her about the mess she had neglected to clean up.  </p>

<p>And low and behold, midway through the clean-up, Sherry suddenly became thankful that someone else was going to have to do it from now on.  </p>

<p>The End.</p>

</body>
</html>