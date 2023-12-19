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

private sealed class Rule(
        val check: (Part) -> Boolean
) {
    class ToWorkflow(
            val nextWorflow: String,
            check: (Part) -> Boolean
    ) : Rule(check)

    class Accept(
            check: (Part) -> Boolean
    ) : Rule(check)

    class Reject(
            check: (Part) -> Boolean
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
                                        val check = if (parts.size == 1) {
                                            { true }
                                        } else {
                                            val checkParts = parts
                                                    .first()
                                                    .split("<", ">")

                                            val res = { p: Part ->
                                                val data = when (checkParts.first()) {
                                                    "x" -> p.x
                                                    "a" -> p.a
                                                    "m" -> p.m
                                                    "s" -> p.s
                                                    else -> throw IllegalArgumentException("Some")
                                                }
                                                if (parts.first().contains(">")) {
                                                    data > checkParts.last().toInt()
                                                } else {
                                                    data < checkParts.last().toInt()
                                                }
                                            }
                                            res
                                        }
                                        when (val result = parts.last()) {
                                            "A" -> Rule.Accept(check)
                                            "R" -> Rule.Reject(check)
                                            else -> Rule.ToWorkflow(
                                                    check = check,
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
                            val rules = currentWorkflow.rules as List<Rule>
                            for (i in 0..<rules.size) {
                                val rule = rules[i]
                                val ruleResult = rule.check.invoke(part)
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

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day19/Day19_test")
    check(part1(testInput) == 19114)
//    check(part2(testInput) == 145)

    val input = readInput("day19/Day19")
    part1(input).println()
//    part2(input).println()
}
