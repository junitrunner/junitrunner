junitrunner
===========

JUnit Runner implementation which allows different frameworks to collaborate in the same test case.

JUnit allows to ehance test classes in a several ways. Most straigh forward and most used way is to use acustom Runner, via @RunWith annotation. And frameworks like Spring, Unitils, Mockito and others provide those custom Runners for our pleasure. Works like a charm until you want to use 2 frameworks at once. You can only have 1 runner.

So I've created a runner which supports Plugins. 
From test case developer perspective it should look like:

```
@Runwith(JUnitRunner.class)
@WithPlugins({SpringPlugin.class,UnitilsPlugin.class})
public class ATest {
...
}
```

It is my hope that eventually JUnit developers will eventually provide runner like this. But so far they are so enthusiastic.
And I hope that framework profiders will start providing / maintaining plugins.
For now, I've converted their Runners to Plugins, which should be fully functional.
