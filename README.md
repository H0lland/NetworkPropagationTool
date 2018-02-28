# NetworkPropagationTool
A tool for testing phenomena propagation in networks with a focus on centrality metrics.

## 1.1 Goal

For this version of this software project, we aim to have software that allows analysis of phenomena propagation in traditional and interdependent networks against various centrality metrics.

## 1.2 Stages of Development

This software project has three central development targets for this version.

1. Implement Network classes using the Java programming language.

> The following classes to support the creation and manipulation of networks must be implemented: `AbstractNetwork`, `Network`, `IDN`, `Node`.
>> \*`IDN` is short for "Interdependent Network"
>
> `AbstractNetwork` is an abstract class that `Network` and `IDN` will extend. `Node` will be used for future iterations to store additional data (or features) pertaining to individual nodes in networks.

2. Develop centrality metrics to be used to find central nodes in a given network.

> Classes must be built to support the algorithmic process of finding central nodes given the logic of a particular centrality metric. The implementation details of this phase have yet to be finalized.

3. Implement phenomena for phenomena propagation.

> Classes must be built to allow the creation of various phenomena. As with centrality metrics, the implementation details have yet to be decided.

## 1.3 Tools

* Text editing software used for this project are GitHub's **Atom** text editor. 
* **Java** programming language will be used for implementation of software.
* Project build management tool for this software is **Gradle**.
	* [Introduction to Gradle Resource](https://spring.io/guides/gs/gradle/#scratch)
