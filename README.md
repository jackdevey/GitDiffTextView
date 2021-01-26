# GitDiffTextView

This is a modified fork of [alorma/GitDiffTextView](https://github.com/alorma/GitDiffTextView), with customised colours and an updated gradle. This package was created primarily for use in [BanDev/Labyrinth](https://github.com/BanDev/Labyrinth), and for any other projects I still recommend using the original library ([alorma/GitDiffTextView](https://github.com/alorma/GitDiffTextView)). However, if you still want to use it I'll add some installation steps below:

## Installation

#### Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

``` groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

#### Add the dependency
Add dependency to your build.gradle file:

``` groovy
	dependencies {
	        implementation 'com.github.jackdevey:GitDiffTextView:1.0.0'
	}
```

## Usage

Add GitDiffTextView to your layout: 

``` xml

<com.jackdevey.gitdifftextview.DiffTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:id="@+id/diffTextView"/>

```

Add diff text:

``` java

DiffTextView diffTextView = (DiffTextView) findViewById(R.id.diffTextView);

String diffText = "@@ -29,6 +29,7 @@
        android:resource=\"@xml/searchable_repos\" />
    </activity>
    <activity android:name=\"com.alorma.github.ui.activity.ProfileActivity\" />
+   <activity android:name=\"com.alorma.github.ui.activity.CommitDetailActivity\" />
    <activity android:name=\"com.alorma.github.ui.activity.RepoDetailActivity\" />
    <activity android:name=\"com.alorma.github.ui.dialog.NewIssueCommentDialog\" />
-   <activity android:name=\"com.alorma.github.ui.activity.FileActivity\" />";

diffTextView.setMaxLines(5);

diffTextView.setText(diffText);

```
