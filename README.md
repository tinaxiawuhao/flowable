# Flowable工作流引擎概要

工作流，是把业务之间的各个步骤以及规则进行抽象和概括性的描述。使用特定的语言为业务流程建模，让其运行在计算机上，并让计算机进行计算和推动。
工作流是复杂版本的状态机。
而对于复杂的状态或者状态维度增加且状态流转的条件极为复杂，可能单纯用字段记录状态的实现方式就会不那么理想。
工作流解决的痛点在于，解除业务宏观流程和微观逻辑的耦合，让熟悉宏观业务流程的人去制定整套流转逻辑，而让专业的人只需要关心他们应当关心的流程节点，就好比大家要一起修建一座超级体育场，路人甲只需要关心他身边的这一堆砖是怎么堆砌而非整座建筑。

**那么工作流有什么不能解决的问题呢？**

工作流是一个固定好的框架，大家就按照这个框架来执行流程就行了，但某些情况他本身没有流转顺序的要求，比如：小明每天需要做作业，做运动以及玩游戏，它们之间没有关联性无法建立流程，但可以根据每一项完成的状态决定今天的任务是否完结，这种情况我们需要使用CMMN来建模，它就是专门针对这种情况而设计的，但今天我们不讲这个，而是讲讲BPMN协议。

# BPMN2.0协议

对于业务建模，我们需要一种通用的语言来描绘，这样在沟通上和实现上会降低难度，就像中文、英文一样，BPMN2.0便是一种国际通用的建模语言，他能让自然人轻松阅读，更能被计算机所解析。
协议中元素的主要分类为，事件-任务-连线-网关。
一个流程必须包含一个事件（如：开始事件）和至少一个结束（事件）。其中网关的作用是流程流转逻辑的控制。任务则分很多类型，他们各司其职，所有节点均由连线联系起来。
下面我就以每种类型的节点简单地概括一下其作用。



## 网关：

- 互斥网关（Exclusive Gateway），又称排他网关，他有且仅有一个有效出口，可以理解为if......else if...... else if......else，就和我们平时写代码的一样。
- 并行网关（Parallel Gateway），他的所有出口都会被执行，可以理解为开多线程同时执行多个任务。
- 包容性网关（Inclusive Gateway），只要满足条件的出口都会执行，可以理解为 if(......) do, if (......) do, if (......) do，所有的条件判断都是同级别的。

## 任务：

- 人工任务（User Task），它是使用得做多的一种任务类型，他自带有一些人工任务的变量，例如签收人（Assignee），签收人就代表该任务交由谁处理，我们也可以通过某个特定或一系列特定的签收人来查找待办任务。利用上面的行为解释便是，当到达User Task节点的时候，节点设置Assignee变量或等待设置Assignee变量，当任务被完成的时候，我们使用Trigger来要求流程引擎退出该任务，继续流转。
- 服务任务（Service Task），该任务会在到达的时候执行一段自动的逻辑并自动流转。从“到达自动执行一段逻辑”这里我们就可以发现，服务任务的想象空间就可以非常大，我们可以执行一段计算，执行发送邮件，执行RPC调用，而使用最广泛的则为HTTP调用，因为HTTP是使用最广泛的协议之一，它可以解决大部分第三方调用问题，在我们的使用中，HTTP服务任务也被我们单独剥离出来作为一个特殊任务节点。
- 接受任务（Receive Task），该任务的名字让人费解，但它又是最简单的一种任务，当该任务到达的时候，它不做任何逻辑，而是被动地等待Trigger，它的适用场景往往是一些不明确的阻塞，比如：一个复杂的计算需要等待很多条件，这些条件是需要人为来判断是否可以执行，而不是直接执行，这个时候，工作人员如果判断可以继续了，那么就Trigger一下使其流转。
- 调用活动（Call Activity），调用活动可以理解为函数调用，它会引用另外一个流程使之作为子流程运行，调用活动跟函数调用的功能一样，使流程模块化，增加复用的可能性。




Flowable以及与其他工作流引擎的对比

Flowable是BPMN2.0协议的一种Java版本的实现。
Flowable项目提供了一组核心的开源业务流程引擎，这些引擎紧凑且高效。它们为开发人员、系统管理员和业务用户提供了一个工作流和业务流程管理（BPM）平台。它的核心是一个非常快速且经过测试的动态BPMN流程引擎。它基于Apache2.0开源协议，有稳定且经过认证的社区。
Flowable可以嵌入Java应用程序中运行，也可以作为服务器、集群运行，更可以提供云服务。
与大多数故事一样，Flowable也是因为其与Activiti对未来规划的路线不认同而开辟了一条自己的道路。目前主流的工作流开源框架就是Activiti/Camunda/Flowable，它们都有一个共同的祖先jbpm。先是有了jbpm4，随后出来了一个Activiti5，Activiti5经过一段时间的发展，核心人员出现分歧，又分出来了一个Camunda。activiti5发展了4年左右，紧接着就出现了Flowable。
相比于Activiti，Flowable的核心思想更像是在做一个多彩的工具，它在工作流的基础功能上，提供了很多其他的扩展，使用者可以随心所欲地把Flowable打造成自己想要的样子。例如：Camel节点，Mule节点。他不仅有bpmn引擎，还有cmmn（案例管理模型），dmn（决策模型），content（内容管理），form（表单管理），idm（用户鉴权）等等，但这也间接导致了Flowable的包结构非常繁多，上手非常困难。
而Activiti则只着重于处理bpmn，它的方向在于云，他的设计会尽量像例如Spring Cloud、Docker、K8S靠拢
如果你单纯地想快速上手bpmn引擎，建议使用Activiti，如果你想做出花样繁多的工作流引擎，建议使用Flowable。
而Camunda（卡蒙达）则更加的轻巧灵活，他的初衷就是为开发人员设计的“小工具”，但我个人的感觉而言，Camunda从代码上看并没有Activiti和Flowable好，而且他的社区是最不活跃的一个（至少从国内的角度来看），所以不太建议使用（当然这带了很多个人主观感受，如有不同意见，欢迎讨论）。
Flowable的核心架构和一些改进
