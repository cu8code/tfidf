#!/bin/env sh

/home/ankan/.jdks/openjdk-19.0.2/bin/java -javaagent:/snap/intellij-idea-community/409/lib/idea_rt.jar=42637:/snap/intellij-idea-community/409/bin -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /home/ankan/git/java/Dsalgo/out/production/Dsalgo:/home/ankan/git/java/Dsalgo/lib/org.json.jar:/home/ankan/git/java/Dsalgo/lib/nanohttpd-2.3.1.jar App "./test/"

