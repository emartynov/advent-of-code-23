package day19

import println
import readInput
import java.lang.IllegalArgumentException

private data class Part(
        val x: Int,
        val m: Int,
        val a: Int,
        val s: Int
)

private data class Possibilities(
        val x: IntRange = 1..4000,
        val m: IntRange = 1..4000,
        val a: IntRange = 1..4000,
        val s: IntRange = 1..4000
) {
    fun all(): Long {
        if (listOf(x.isEmpty(), m.isEmpty(), a.isEmpty(), s.isEmpty()).any { it })
            return 0L

        return (x.last - x.first + 1).toLong() *
                (m.last - m.first + 1) *
                (a.last - a.first + 1) *
                (s.last - s.first + 1)
    }

}

private operator fun IntRange.minus(range: IntRange): IntRange =
        if (isEmpty()) {
            this
        } else {
            val first = if (range.first == first) range.last + 1 else first
            val last = if (range.last == last) range.first - 1 else last
            first..last
        }

private sealed class Operation {
    abstract fun check(p: Part): Boolean

    data object Pass : Operation() {
        override fun check(p: Part): Boolean {
            return true
        }
    }

    data class Less(val partComponent: String, val value: Int) : Operation() {
        override fun check(p: Part): Boolean {
            val partComponentValue =
                    when (partComponent) {
                        "x" -> p.x
                        "a" -> p.a
                        "m" -> p.m
                        "s" -> p.s
                        else -> throw IllegalArgumentException("Some")
                    }

            return partComponentValue < value
        }
    }

    data class More(val partComponent: String, val value: Int) : Operation() {
        override fun check(p: Part): Boolean {
            val partComponentValue =
                    when (partComponent) {
                        "x" -> p.x
                        "a" -> p.a
                        "m" -> p.m
                        "s" -> p.s
                        else -> throw IllegalArgumentException("Some")
                    }
            return partComponentValue > value
        }
    }
}

private sealed class Rule(
        val operation: Operation
) {
    class ToWorkflow(
            val nextWorflow: String,
            check: Operation
    ) : Rule(check)

    class Accept(
            check: Operation
    ) : Rule(check)

    class Reject(
            check: Operation
    ) : Rule(check)
}

private data class Workflow(
        val name: String,
        val rules: List<Rule>
)

fun main() {
    fun List<String>.parsePartsAndRules(): Pair<Map<String, Workflow>, List<Part>> {
        val indexOfSplit = indexOfFirst { it.isBlank() }

        val workfows =
                subList(0, indexOfSplit)
                        .map { workflowString ->
                            val name = workflowString.substringBefore("{")
                            val rulesString = workflowString
                                    .substringAfter("{")
                                    .dropLast(1)
                            val rules = rulesString
                                    .split(",")
                                    .map { ruleString ->
                                        val parts = ruleString.split(":")
                                        val operation = if (parts.size == 1) {
                                            Operation.Pass
                                        } else {
                                            val checkParts = parts
                                                    .first()
                                                    .split("<", ">")

                                            if (parts.first().contains(">")) {
                                                Operation.More(
                                                        partComponent = checkParts.first(),
                                                        value = checkParts.last().toInt()
                                                )
                                            } else {
                                                Operation.Less(
                                                        partComponent = checkParts.first(),
                                                        value = checkParts.last().toInt()
                                                )
                                            }
                                        }
                                        when (val result = parts.last()) {
                                            "A" -> Rule.Accept(operation)
                                            "R" -> Rule.Reject(operation)
                                            else -> Rule.ToWorkflow(
                                                    check = operation,
                                                    nextWorflow = result
                                            )
                                        }
                                    }
                            Workflow(
                                    name = name,
                                    rules = rules
                            )
                        }

        val parts = subList(indexOfSplit + 1, size)
                .map { partString ->
                    val parts = partString
                            .substring(1..partString.length - 2)
                            .split(",")
                            .associate { componentString ->
                                componentString
                                        .split("=")
                                        .let {
                                            it.first() to it.last().toInt()
                                        }
                            }
                    Part(
                            x = parts.getValue("x"),
                            m = parts.getValue("m"),
                            a = parts.getValue("a"),
                            s = parts.getValue("s"),
                    )
                }

        return workfows.associateBy { it.name } to parts
    }

    fun part1(input: List<String>): Int {
        return input
                .parsePartsAndRules()
                .let { (workflows, parts) ->
                    parts.filter { part ->
                        var accepted = false
                        var currentWorkflow: Workflow? = workflows["in"]
                        while (currentWorkflow != null) {
                            val rules = currentWorkflow.rules
                            for (i in 0..<rules.size) {
                                val rule = rules[i]
                                val ruleResult = rule.operation.check(part)
                                if (ruleResult) {
                                    if (rule is Rule.ToWorkflow) {
                                        currentWorkflow = workflows[rule.nextWorflow]
                                    } else {
                                        accepted = rule is Rule.Accept
                                        currentWorkflow = null
                                    }
                                    break
                                }
                            }
                        }
                        accepted
                    }
                }
                .sumOf { part ->
                    part.a + part.m + part.s + part.x
                }
    }

    fun caclulatePossibilities(possibilities: Possibilities, key: String, workflows: Map<String, Workflow>): Long {
        var leftPossibilities = possibilities
        return workflows.getValue(key)
                .rules
                .sumOf { rule ->
                    when (rule) {
                        is Rule.Accept -> {
                            val acceptedPossibilities = when (rule.operation) {
                                is Operation.Less -> {
                                    Possibilities(
                                            a = if (rule.operation.partComponent == "a") {
                                                minOf(leftPossibilities.a.first, rule.operation.value - 1)..
                                                        minOf(leftPossibilities.a.last, rule.operation.value - 1)
                                            } else {
                                                leftPossibilities.a
                                            },
                                            x = if (rule.operation.partComponent == "x") {
                                                minOf(leftPossibilities.x.first, rule.operation.value - 1)..
                                                        minOf(leftPossibilities.x.last, rule.operation.value - 1)
                                            } else {
                                                leftPossibilities.x
                                            },
                                            s = if (rule.operation.partComponent == "s") {
                                                minOf(leftPossibilities.s.first, rule.operation.value - 1)..
                                                        minOf(leftPossibilities.s.last, rule.operation.value - 1)
                                            } else {
                                                leftPossibilities.s
                                            },
                                            m = if (rule.operation.partComponent == "m") {
                                                minOf(leftPossibilities.m.first, rule.operation.value - 1)..
                                                        minOf(leftPossibilities.m.last, rule.operation.value - 1)
                                            } else {
                                                leftPossibilities.m
                                            },
                                    )
                                }

                                is Operation.More -> {
                                    Possibilities(
                                            a = if (rule.operation.partComponent == "a") {
                                                maxOf(leftPossibilities.a.first, rule.operation.value + 1)..
                                                        maxOf(leftPossibilities.a.last, rule.operation.value + 1)
                                            } else {
                                                leftPossibilities.a
                                            },
                                            x = if (rule.operation.partComponent == "x") {
                                                maxOf(leftPossibilities.x.first, rule.operation.value + 1)..
                                                        maxOf(leftPossibilities.x.last, rule.operation.value + 1)
                                            } else {
                                                leftPossibilities.x
                                            },
                                            s = if (rule.operation.partComponent == "s") {
                                                maxOf(leftPossibilities.s.first, rule.operation.value + 1)..
                                                        maxOf(leftPossibilities.s.last, rule.operation.value + 1)
                                            } else {
                                                leftPossibilities.s
                                            },
                                            m = if (rule.operation.partComponent == "m") {
                                                maxOf(leftPossibilities.m.first, rule.operation.value + 1)..
                                                        maxOf(leftPossibilities.m.last, rule.operation.value + 1)
                                            } else {
                                                leftPossibilities.m
                                            },
                                    )
                                }

                                Operation.Pass -> leftPossibilities
                            }
                            leftPossibilities = when (rule.operation) {
                                is Operation.Pass -> Possibilities(
                                        IntRange.EMPTY,
                                        IntRange.EMPTY,
                                        IntRange.EMPTY,
                                        IntRange.EMPTY,
                                )

                                else -> {
                                    val partName = if (rule.operation is Operation.Less) {
                                        rule.operation.partComponent
                                    } else {
                                        (rule.operation as Operation.More).partComponent
                                    }
                                    Possibilities(
                                            a = if (partName == "a") (leftPossibilities.a - acceptedPossibilities.a) else leftPossibilities.a,
                                            m = if (partName == "m") (leftPossibilities.m - acceptedPossibilities.m) else leftPossibilities.m,
                                            s = if (partName == "s") (leftPossibilities.s - acceptedPossibilities.s) else leftPossibilities.s,
                                            x = if (partName == "x") (leftPossibilities.x - acceptedPossibilities.x) else leftPossibilities.x,
                                    )
                                }
                            }
                            acceptedPossibilities.all()
                        }

                        is Rule.Reject -> {
                            leftPossibilities = when (rule.operation) {
                                is Operation.Less -> {
                                    Possibilities(
                                            a = if (rule.operation.partComponent == "a") {
                                                maxOf(leftPossibilities.a.first, rule.operation.value)..
                                                        maxOf(leftPossibilities.a.last, rule.operation.value)
                                            } else {
                                                leftPossibilities.a
                                            },
                                            x = if (rule.operation.partComponent == "x") {
                                                maxOf(leftPossibilities.x.first, rule.operation.value)..
                                                        maxOf(leftPossibilities.x.last, rule.operation.value)
                                            } else {
                                                leftPossibilities.x
                                            },
                                            s = if (rule.operation.partComponent == "s") {
                                                maxOf(leftPossibilities.s.first, rule.operation.value)..
                                                        maxOf(leftPossibilities.s.last, rule.operation.value)
                                            } else {
                                                leftPossibilities.s
                                            },
                                            m = if (rule.operation.partComponent == "m") {
                                                maxOf(leftPossibilities.m.first, rule.operation.value)..
                                                        maxOf(leftPossibilities.m.last, rule.operation.value)
                                            } else {
                                                leftPossibilities.m
                                            },
                                    )
                                }

                                is Operation.More -> {
                                    Possibilities(
                                            a = if (rule.operation.partComponent == "a") {
                                                minOf(leftPossibilities.a.first, rule.operation.value)..
                                                        minOf(leftPossibilities.a.last, rule.operation.value)
                                            } else {
                                                leftPossibilities.a
                                            },
                                            x = if (rule.operation.partComponent == "x") {
                                                minOf(leftPossibilities.x.first, rule.operation.value)..
                                                        minOf(leftPossibilities.x.last, rule.operation.value)
                                            } else {
                                                leftPossibilities.x
                                            },
                                            s = if (rule.operation.partComponent == "s") {
                                                minOf(leftPossibilities.s.first, rule.operation.value)..
                                                        minOf(leftPossibilities.s.last, rule.operation.value)
                                            } else {
                                                leftPossibilities.s
                                            },
                                            m = if (rule.operation.partComponent == "m") {
                                                minOf(leftPossibilities.m.first, rule.operation.value)..
                                                        minOf(leftPossibilities.m.last, rule.operation.value)
                                            } else {
                                                leftPossibilities.m
                                            },
                                    )
                                }

                                Operation.Pass -> Possibilities(
                                        x = IntRange.EMPTY,
                                        m = IntRange.EMPTY,
                                        a = IntRange.EMPTY,
                                        s = IntRange.EMPTY,
                                )
                            }
                            0L
                        }

                        is Rule.ToWorkflow -> {
                            val nextWorkflowPossibilities = when (rule.operation) {
                                is Operation.Less -> {
                                    Possibilities(
                                            a = if (rule.operation.partComponent == "a") {
                                                minOf(leftPossibilities.a.first, rule.operation.value - 1)..
                                                        minOf(leftPossibilities.a.last, rule.operation.value - 1)
                                            } else {
                                                leftPossibilities.a
                                            },
                                            x = if (rule.operation.partComponent == "x") {
                                                minOf(leftPossibilities.x.first, rule.operation.value - 1)..
                                                        minOf(leftPossibilities.x.last, rule.operation.value - 1)
                                            } else {
                                                leftPossibilities.x
                                            },
                                            s = if (rule.operation.partComponent == "s") {
                                                minOf(leftPossibilities.s.first, rule.operation.value - 1)..
                                                        minOf(leftPossibilities.s.last, rule.operation.value - 1)
                                            } else {
                                                leftPossibilities.s
                                            },
                                            m = if (rule.operation.partComponent == "m") {
                                                minOf(leftPossibilities.m.first, rule.operation.value - 1)..
                                                        minOf(leftPossibilities.m.last, rule.operation.value - 1)
                                            } else {
                                                leftPossibilities.m
                                            },
                                    )
                                }

                                is Operation.More -> {
                                    Possibilities(
                                            a = if (rule.operation.partComponent == "a") {
                                                maxOf(leftPossibilities.a.first, rule.operation.value + 1)..
                                                        maxOf(leftPossibilities.a.last, rule.operation.value + 1)
                                            } else {
                                                leftPossibilities.a
                                            },
                                            x = if (rule.operation.partComponent == "x") {
                                                maxOf(leftPossibilities.x.first, rule.operation.value + 1)..
                                                        maxOf(leftPossibilities.x.last, rule.operation.value + 1)
                                            } else {
                                                leftPossibilities.x
                                            },
                                            s = if (rule.operation.partComponent == "s") {
                                                maxOf(leftPossibilities.s.first, rule.operation.value + 1)..
                                                        maxOf(leftPossibilities.s.last, rule.operation.value + 1)
                                            } else {
                                                leftPossibilities.s
                                            },
                                            m = if (rule.operation.partComponent == "m") {
                                                maxOf(leftPossibilities.m.first, rule.operation.value + 1)..
                                                        maxOf(leftPossibilities.m.last, rule.operation.value + 1)
                                            } else {
                                                leftPossibilities.m
                                            },
                                    )
                                }

                                Operation.Pass -> leftPossibilities
                            }
                            leftPossibilities = when (rule.operation) {
                                is Operation.Pass -> Possibilities(
                                        IntRange.EMPTY,
                                        IntRange.EMPTY,
                                        IntRange.EMPTY,
                                        IntRange.EMPTY,
                                )

                                else -> {
                                    val partName = if (rule.operation is Operation.Less) {
                                        rule.operation.partComponent
                                    } else {
                                        (rule.operation as Operation.More).partComponent
                                    }
                                    Possibilities(
                                            a = if (partName == "a") (leftPossibilities.a - nextWorkflowPossibilities.a) else leftPossibilities.a,
                                            m = if (partName == "m") (leftPossibilities.m - nextWorkflowPossibilities.m) else leftPossibilities.m,
                                            s = if (partName == "s") (leftPossibilities.s - nextWorkflowPossibilities.s) else leftPossibilities.s,
                                            x = if (partName == "x") (leftPossibilities.x - nextWorkflowPossibilities.x) else leftPossibilities.x,
                                    )
                                }
                            }
                            caclulatePossibilities(
                                    possibilities = nextWorkflowPossibilities,
                                    key = rule.nextWorflow,
                                    workflows = workflows
                            )
                        }
                    }
                }
    }

    fun part2(input: List<String>): Long {
        return input
                .parsePartsAndRules()
                .let { (workflows, _) ->
                    val possibilities = Possibilities()
                    caclulatePossibilities(
                            possibilities = possibilities,
                            key = "in",
                            workflows = workflows
                    )
                }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day19/Day19_test")
    check(part1(testInput) == 19114)
    check(part2(testInput) == 167409079868000)

    val input = readInput("day19/Day19")
    part1(input).println()
    part2(input).println()
}
