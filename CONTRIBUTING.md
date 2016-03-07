# Contributing to Apollo

First off, thanks for taking the time to contribute!   
Apollo is a high-performance, modular RuneScape emulator with a collection of utilities for managing data files and plugins.  
The following is a set of guidelines for contributing to Apollo on GitHub.

* [Forking](#forking)
* [Submission Guidelines](#submit)
    *  [Submitting an Issue](#submit-issue)  
    *  [Submitting a Pull Request](#submit-pull-request)    
* [Coding Style](#coding-style)  
* [Got a Question or Problem](#got-a-question-or-problem)

## Forking 
You should fork the repository first. This step is needed only once. See complete help in github
`git clone https://github.com/apollo-rsps/apollo.git`  
`cd apollo`  
`git remote add upstream https://github.com/apollo-rsps/apollo.git`  
`git fetch upstream`  

Keep your fork up to date, pull in upstream changes:  
`git fetch upstream`  
`git merge upstream/master`  

## Submission Guidelines
### Submitting an Issue

Before you submit your issue search the archive, maybe your question was already answered.
Related Issues - has a similar issue been reported before?
Suggest a Fix - if you can't fix the bug yourself, perhaps you can point to what might be causing the problem (line of code or commit)

### Submitting a Pull Request
Run all the tests to assure nothing else was accidentally broken. 

- Create a new branch: `git branch <branch-name>`  
- Switch to the new branch: `git checkout <branch-name>`   
- Add your change: `git add <file-name>`   
- Comment your change: `git commit -m "my-PR-comment"`  
- Push your branch to GitHub: `git push -u origin <branch-name>`    

If you need to rebase your branch, The git book has a very good guide on doing this (and some more information about rebasing), which you can find [here](https://git-scm.com/book/en/v2/Git-Branching-Rebasing).
## Coding Style

Please follow the [Java styleguides](http://www.oracle.com/technetwork/java/codeconvtoc-136057.html) or [Ruby styleGuides](http://api.rubyonrails.org).

## Got a Question or Problem?

Most discussion related to the development of Apollo happens on the IRC channel #apollorsps on irc.freenode.net.  
Please contact us if you need help!
