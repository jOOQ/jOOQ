To PR or not to PR?
===================

Thanks for offering your help. We really appreciate your intentions. But let's review whether a PR is really reasonable for what you're trying to do.

- **Did you discover a typo or minor bug that you think is really easy to fix?** A PR is perfect. Skip to [The ideal PR](#the-ideal-pr)
- **Are you about to suggest a new feature, API, etc?** Please don't! We're very very likely to reject your PR and you will have wasted your time. Please use the [issue tracker](https://github.com/jOOQ/jOOQ/issues/new) or the [mailing list](https://groups.google.com/d/forum/jooq-user) to discuss your ideas first. Then, quite possibly, it's much more reasonable to leave the implementation to us. Here's why:

Overlooking things
------------------

First and foremost, from our experience, most contributors are overlooking a ton of things. That's because jOOQ has become quite complex over the past 10 years. They will apply a small change to parts of the API without...:

- thinking about all the remaining API that deals with similar topics that needs to be considered
- thinking about secondary features, like the parser (and its grammar), that needs to be updated
- thinking about possible existing functionality, that should be merged with this one
- thinking about possible deprecations that need to be implemented
- thinking about backwards compatibility in its many subtle ways (source, binary, behavioural)
- thinking about the many places in the manual that need to be updated
- thinking about the fact that jOOQ is dual licensed, and your contribution won't work on commercial database (but it has to, so that'll be our job again)

Often, workarounds are good enough
----------------------------------

But even more importantly, just because you have an itch *right now*, doesn't mean it needs to be scratched *right now* (with new API). Quite possibly, a simple workaround will do just as well for you, and it won't cause all of the above efforts both on your side and on our side. In fact, we've become really good at listing 3-4 useful workarounds (using features you probably don't know yet) for just about any problem (which we've probably even seen before). Just because you're about to make a PR doesn't mean we'll prioritise merging it. In fact, experience shows that we hardly ever merge them *at all*!

Product roadmap
---------------

Over time, we will remember the many itches reported by jOOQ users and consolidate them into a much more thoroughly designed new feature that helps many other users, not just your immediate itch. It will be much easier to maintain and have much better quality.

Unfortunately, Github doesn't allow for turning on the issue tracker (very well done and useful) without turning on the PR subsystem (which seems to hint at users that they can contribute *anything* to *anyone* and reasonably expect the contribution to be merged). Think about the OpenJDK. Would you contribute tons of new methods to the `java.util.Collection` API without thoroughly discussing things first on the many OpenJDK mailing lists? Probably not. The same is true here.

Thanks a lot for your understanding, and we're definitely looking forward to your suggestions and discussions on the [issue tracker](https://github.com/jOOQ/jOOQ/issues/new) or the [mailing list](https://groups.google.com/d/forum/jooq-user)

The ideal PR
============

Thanks again for offering your help. This section is for PRs that fix simple issues, which don't need any discussion - like typos, minor bugs, etc.

In order to make our cooperation as smooth as possible, we have a couple of guidelines that we'd like you to follow:

- **If in doubt, please discuss your ideas first before providing a pull request. See above.**
- Fork the repository.
- Check out and work on your own fork.
- Try to make your commits as atomic as possible. Related changes to three files should be committed in one commit.
- The commit message should reference the issue number, e.g. `[#5873] Add Setting.quoteEscaping to allow for alternative escaping of single quotes`.
- Try not to modify anything unrelated, i.e. don't make unimportant whitespace / formatting changes, which will just distract during review.
- **In particular, please do not remove excess whitespace / unnecessary imports etc. The jOOQ Open Source Edition is a "slave clone" of the commercial jOOQ distributions. We'd like to keep all line numbers intact between the two versions!**
- Don't worry about tests. Our unit and integration tests are not open source. If your change is substantial, we'll add tests ourselves.
- Add your name to our [jOOQ/src/main/resources/META-INF/ABOUT.txt](https://github.com/jOOQ/jOOQ/blob/master/jOOQ/src/main/resources/META-INF/ABOUT.txt) file
- Be sure you agree to transfer your rights to us (see below) before contributing.

Contributing to dual-licensed Open Source
=========================================

Thank you very much for contributing to jOOQ.

jOOQ is dual-licensed Open Source software. While the version published here on GitHub is [ASL 2.0](http://www.apache.org/licenses/LICENSE-2.0) licensed, we also ship jOOQ under a commercial license to those customers who want to use jOOQ with commercial databases. For more information about commercial licensing, please refer to this website:
[http://www.jooq.org/legal/licensing](http://www.jooq.org/legal/licensing)

Our dual-licensing means that we need you to tranfer all rights to your contribution to us, [Data Geekery GmbH](http://www.datageekery.com), in order to be able to re-license your contribution also to our commercial customers, if your contribution consists in any of the following:

- Source code to be embedded into jOOQ deliverables
- Source code to be embedded into jOOQ integration tests
- Content to be embedded into our manual

No transfer of rights is needed for the following:

- Bug reports
- Feature requests and discussions
- Example code to reproduce bugs (e.g. through https://github.com/jOOQ/jOOQ-mcve)

If your contribution requires a transfer of rights, please sign the following document before proceeding:
http://www.jooq.org/legal/contributions

Thank you again very much for your contribution
The jOOQ Team
