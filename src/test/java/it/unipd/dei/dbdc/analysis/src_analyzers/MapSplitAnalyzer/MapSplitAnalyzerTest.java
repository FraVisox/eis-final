package it.unipd.dei.dbdc.analysis.src_analyzers.MapSplitAnalyzer;

import it.unipd.dei.dbdc.analysis.Article;
import it.unipd.dei.dbdc.analysis.OrderedEntryStringInt;
import it.unipd.dei.dbdc.analysis.interfaces.Analyzer;
import it.unipd.dei.dbdc.analysis.interfaces.UnitOfSearch;
import it.unipd.dei.dbdc.analysis.src_analyzers.PriorityQueueSplitAnalyzer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The class that tests {@link MapSplitAnalyzer} and {@link PriorityQueueSplitAnalyzer}.
 * The first class is the one used in the program, the second one is tested at the beginning and then is
 * used for the tests of the first class with many articles.
 * It is tested after {@link it.unipd.dei.dbdc.download.src_api_managers.TheGuardianAPI.GuardianAPIManagerTest}.
 */
@Order(7)
public class MapSplitAnalyzerTest {

    /**
     * The instance of {@link MapSplitAnalyzer} to test.
     */
    private static final Analyzer toTest = new MapSplitAnalyzer();

    /**
     * The instance of {@link PriorityQueueSplitAnalyzer} to test.
     */
    private static final Analyzer toCheck = new PriorityQueueSplitAnalyzer();

    /**
     * Tests of {@link MapSplitAnalyzer#mostPresent(List, int, Set)} and {@link PriorityQueueSplitAnalyzer#mostPresent(List, int, Set)}.
     * Firstly we test both with inputs that are easy to process by hand, then we use the second one to test the first one.
     */
    @Test
    public void mostPresent()
    {
        List<UnitOfSearch> articles = new ArrayList<>();
        Set<String> banned = new HashSet<>();

        //Tests with null or empty
        assertEquals(0, toTest.mostPresent(null, 50, banned).size());
        assertEquals(0, toTest.mostPresent(articles, 50, banned).size());

        articles.add(new Article(new String[]{"id", "url", "this is the headline", "this is the body", "date", null, null}));
        assertEquals(0, toTest.mostPresent(articles, -1, banned).size());
        assertEquals(0, toTest.mostPresent(articles, 0, banned).size());

        //Banned can be null
        assertEquals(5, toTest.mostPresent(articles, 50, banned).size());
        assertEquals(5, toTest.mostPresent(articles, 50, null).size());

        //Tries with not initialized articles
        articles.add(new Article(new String[]{"id", "url", null, "this is the body", "date", null, null}));
        assertThrows(IllegalArgumentException.class, () -> toTest.mostPresent(articles, 50, banned));

        articles.clear();
        articles.add(new Article(new String[]{"id", "url", "null", null, "date", null, null}));
        assertThrows(IllegalArgumentException.class, () -> toTest.mostPresent(articles, 50, banned));

        articles.clear();
        articles.add(new Article());
        assertThrows(IllegalArgumentException.class, () -> toTest.mostPresent(articles, 50, banned));

        //Tests with valid articles
        List<OrderedEntryStringInt> expected = new ArrayList<>();
        List<OrderedEntryStringInt> obtained;

        articles.clear();
        articles.add(new Article(new String[]{"id", "url", "null", "null", "date", null, null}));
        expected.add(new OrderedEntryStringInt("null", 1));
        obtained = toTest.mostPresent(articles, 50, banned);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        assertEquals(toCheck.mostPresent(articles, 50, banned), obtained);

        articles.clear();
        expected.clear();
        articles.add(new Article(new String[]{"id", "url", "null", "null", "date", null, null}));
        articles.add(new Article(new String[]{"id", "url", "null", "null", "date", null, null}));
        articles.add(new Article(new String[]{"id", "url", "null", "null", "date", null, null}));
        articles.add(new Article(new String[]{"id", "url", "null", "null", "date", null, null}));
        articles.add(new Article(new String[]{"id", "url", "null", "null", "date", null, null}));
        expected.add(new OrderedEntryStringInt("null", 5));
        obtained = toTest.mostPresent(articles, 50, banned);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        assertEquals(toCheck.mostPresent(articles, 50, banned), obtained);

        articles.clear();
        expected.clear();
        articles.add(new Article(new String[]{"id", "url", "i am an article", "i am not", "date", null, null}));
        articles.add(new Article(new String[]{"id", "url", "yes i was", "", "date", null, null}));
        articles.add(new Article(new String[]{"id", "url", "not i", "null", "date", null, null}));
        articles.add(new Article(new String[]{"id", "url", "????---2324as&&7-i--was", "null", "date", null, null}));
        articles.add(new Article(new String[]{"id", "url", "       u ", "           i ", "date", null, null}));
        expected.add(new OrderedEntryStringInt("i", 5));
        expected.add(new OrderedEntryStringInt("not", 2));
        expected.add(new OrderedEntryStringInt("null", 2));
        expected.add(new OrderedEntryStringInt("was", 2));
        expected.add(new OrderedEntryStringInt("am", 1));
        expected.add(new OrderedEntryStringInt("an", 1));
        expected.add(new OrderedEntryStringInt("article", 1));
        expected.add(new OrderedEntryStringInt("as", 1));
        expected.add(new OrderedEntryStringInt("u", 1));
        expected.add(new OrderedEntryStringInt("yes", 1));
        obtained = toTest.mostPresent(articles, 50, banned);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        assertEquals(toCheck.mostPresent(articles, 50, banned), obtained);

        //With a banned word
        banned.add("null");
        expected.clear();
        expected.add(new OrderedEntryStringInt("i", 5));
        expected.add(new OrderedEntryStringInt("not", 2));
        expected.add(new OrderedEntryStringInt("was", 2));
        expected.add(new OrderedEntryStringInt("am", 1));
        expected.add(new OrderedEntryStringInt("an", 1));
        expected.add(new OrderedEntryStringInt("article", 1));
        expected.add(new OrderedEntryStringInt("as", 1));
        expected.add(new OrderedEntryStringInt("u", 1));
        expected.add(new OrderedEntryStringInt("yes", 1));
        obtained = toTest.mostPresent(articles, 50, banned);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        assertEquals(toCheck.mostPresent(articles, 50, banned), obtained);

        //With a number of words that is minor than the total number of words
        expected.clear();
        expected.add(new OrderedEntryStringInt("i", 5));
        expected.add(new OrderedEntryStringInt("not", 2));
        expected.add(new OrderedEntryStringInt("was", 2));
        expected.add(new OrderedEntryStringInt("am", 1));
        expected.add(new OrderedEntryStringInt("an", 1));
        obtained = toTest.mostPresent(articles, 5, banned);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        assertEquals(toCheck.mostPresent(articles, 5, banned), obtained);

        expected.clear();
        expected.add(new OrderedEntryStringInt("i", 5));
        expected.add(new OrderedEntryStringInt("not", 2));
        obtained = toTest.mostPresent(articles, 2, banned);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        assertEquals(toCheck.mostPresent(articles, 2, banned), obtained);

        expected.clear();
        expected.add(new OrderedEntryStringInt("i", 5));
        obtained = toTest.mostPresent(articles, 1, banned);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        assertEquals(toCheck.mostPresent(articles, 1, banned), obtained);

        articles.clear();
        articles.add(new Article(null, null, "null Hard Questions on Nuclear Power", "After decades in the doghouse because of environmental, safety and cost concerns, nuclear power is enjoying a renaissance of expectations. The Bush administration's new energy plan gives a place of prominence to nuclear power as a clean and efficient energy source, and the industry itself is bubbling with new hopes and plans. In truth, there are good reasons to take a fresh look at this much-maligned source of energy that has been stalled in this country for the past three decades. But it is worrisome that the administration seems to have endorsed a nuclear resurgence with little sustained analysis of its pluses and minuses. As an article by Katharine Seelye in The Times revealed last week, nuclear energy was barely in the consciousness of the drill-centric energy team at the White House until a delegation of nuclear industry executives sought a chance to make their pitch and succeeded so well that Vice President Dick Cheney almost immediately started touting the virtues of nuclear energy. A case can be made for greater exploitation of nuclear power in this country, but before the nation plunges too far down this path the administration will need to address some critical questions. The rationale for a reassessment comes partly from the performance of the industry itself, and partly from changed circumstances in the environment in which it must operate. By most accounts, the industry has learned to operate its plants more safely and efficiently than in the years leading up to the traumatic near-tragedy at Three Mile Island. American nuclear plants are operating with much greater reliability and many fewer minor incidents. Moreover, the industry is consolidating, with plants being purchased and operated by companies that have more expertise than some of the previous operators. So there is reason to trust the industry a bit more than in past decades. Meanwhile, external events are increasing the appeal of nuclear power. One is the rising concern over global warming, which is caused primarily by the emission of carbon dioxide from burning fossil fuels. Nuclear power plants emit no carbon dioxide, thus to the extent they can replace plants burning coal, oil or natural gas they can be considered a plus. Nuclear power can also contribute to the diversity of the nation's energy supplies. Nuclear plants already supply some 20 percent of the electricity generated in this country, compared with fossil fuel contributions of 52 percent for coal, 16 percent for natural gas and 3 percent for oil. But the great majority of all new power plants are being built to burn natural gas, the cleanest of the fossil fuels, making utilities and consumers vulnerable to price spikes when supplies become tight as they have this year. President Bush's energy plan offers a wide range of steps to accelerate the use of nuclear power. But before Congress and the regulatory agencies proceed too far, some crucial questions require answers. Impact on global warming: If this is the main reason for turning to nuclear power, the proponents will need to do a much better job of spelling out just how far nuclear power would have to spread to make a real dent in the problem. Nuclear power is used almost exclusively to generate electricity, thus it cannot reduce the nation's reliance on imported oil to power transportation systems. Nuclear fuel will primarily be substituting for natural gas -- the least of the carbon dioxide emitters -- as the clean fuel to which electric utilities turn. Moreover, fossil fuels are burned in mining and preparing nuclear fuels and in building reactors, so even nuclear energy is not entirely free of greenhouse gases. Some analyses suggest that to make a real impact in slowing global warming, nuclear power plants would need to spread widely around the world, a prospect that brings new challenges of its own. Weapons risks: Expansion of nuclear power in this country poses no weapons danger, but the spread of nuclear plants into other countries could pose a risk. The uranium fuel for nuclear power plants is not generally considered of high enough grade to be used in weapons. But as more and more technicians around the world learn the skills of working with nuclear materials, and as governments become engaged in procuring nuclear technologies, there is a danger that civilian nuclear programs could serve as a cover for clandestine weapons activities. That is why, for example, the United States is angry that Russia is helping Iran build a nuclear power plant. Even though Iran has pledged to abide by nonproliferation treaties and allow international inspections of the plant, there is grave concern that it will find a way to build weapons. Increasing the use of nuclear power in countries that already have either the bomb or nuclear power plants is not much of a danger. Spreading nuclear power to additional countries might be. Waste disposal: In the political world, the lack of a proven method to store spent fuel from nuclear reactors for the tens of thousands of years the material remains radioactive has long been considered the Achilles' heel of the nuclear industry. In truth, spent fuel has been stored safely for decades in pools at the sites of nuclear power plants with no adverse effect. The problem is, the storage pools are filling up and critics are loath to expand nuclear power with no clear idea where to store the waste. The Bush administration is considering a site at Yucca Mountain in Nevada that has been studied for years, and it has proposed a new look at reprocessing the fuel to remove the long-lived plutonium for reuse as reactor fuel. That could greatly ease the storage problem here but might encourage wider use of reprocessed materials abroad, increasing the risk of weapons-grade plutonium's falling into the wrong hands. Reactor safety: The safety problem in conventional nuclear plants is that, if things start to go wrong, emergency cooling systems and human operators have to act correctly to prevent a catastrophic meltdown. That makes nuclear power a cruel and unforgiving technology that cannot tolerate equipment failures or human mistakes. But the industry is exploring new technologies that would not lead to meltdown even in a worst-case malfunction, making them inherently safer and cheaper to build and operate. This is where the administration and the industry should be focusing their efforts -- to develop demonstrably safer power plants. That would ease many of the concerns provoked by the current generation of nuclear reactors. Economics: No matter what else is done to make nuclear power more attractive, the industry will make little headway unless it can overcome the high capital costs that brought it to a halt in recent decades. Some relief should come from the advance approval of standardized designs, allowing plants to be built more quickly and cheaply than in past years when each plant had a customized design. But Congress will need to take a close look at whether it should renew one of the industry's economic underpinnings -- the so-called Price-Anderson Act that limits the liability of nuclear companies in the event of an accident. If the industry is as safe as it says, it may not need such subsidized protection. On the other hand, eliminating the liability protection might scare off investors for good. Nuclear power has been stalled for so long in the United States that it is surprising to see it back in the spotlight. There may be a case for extending the licenses of existing plants, as has already happened in several cases, or for building new plants on existing nuclear sites where the risks are already understood. But the case has not yet been made for truly large-scale expansion of nuclear power, in this country or around the globe", null, null, null));
        articles.add(new Article(null, null, "null As Demand for Electricity Increases, China Sticks With Nuclear Power", "  BEIJING Meltdowns of three reactors at the Fukushima Daiichi nuclear power plant in Japan last March have put a chill on much of the world’s nuclear power industry — but not in China. The German Parliament voted this summer to close the country’s remaining nuclear power plants by 2022, while the Swiss Parliament voted this summer to phase nuclear power out by 2034. Economic stagnation in the United States and most other industrialized economies since 2008 has produced stagnant electricity demand, further sapping interest in nuclear power. In Japan itself, the government has put on hold its plans to build further nuclear power plants, and the government faces a political battle to continue operating existing reactors. Emerging economies like Brazil and particularly India are still planning nuclear reactors. But the Indian leaders introduced legislation on Sept. 7 that is supposed to increase the independence of nuclear regulators from the industry, though critics are skeptical. That leaves China poised to build more nuclear reactors in the coming years than the rest of the world combined. But questions abound whether China will be a savior for the international nuclear power industry, or a ferocious competitor that could create even greater challenges for nuclear power companies in the West. Chinese regulators performed a four-month review of safety at all existing nuclear reactors and reactors under construction after the Fukushima meltdowns and declared them safe. Safety reviews continue at reactors where construction had not yet started at the time of the Fukushima accident. Jiang Kejun, a director of the Energy Research Institute at the National Development and Reform Commission, the top Chinese economic planning agency, said that the government was sticking to its target of 50 gigawatts of nuclear power by 2015, compared to just 10.8 gigawatts at the end of last year. Mr. Jiang said in an interview that nuclear power construction targets for 2020 had not yet been set and might end up slightly lower than they would have been without the meltdowns in Fukushima. But he and other Chinese officials say that China’s rapidly rising electricity consumption makes nuclear power essential. They even try to portray the Fukushima incidents as salutary for the nuclear power industry, a view seldom heard elsewhere. “Globally, I think Fukushima could be a good thing for nuclear power, Mr. Jiang said. “We can learn a lot from that. We can’t be smug or too clever. China allowed existing reactors to continue operating during the safety review from March to July and allowed construction to continue at reactors where it had already begun. Chinese regulators have also encouraged electric utilities to continue planning future nuclear power plants. But one category of reactor has been delayed by the Fukushima incident. At reactors that had been approved before the Fukushima accident but where construction had not yet begun, the government still has not allowed construction to start while continuing to study whether further safety improvements can be made, said Xu Yuanhui, one of China’s top nuclear engineers for the past half century. The delay applies to several conventional nuclear reactors plus Beijing’s project to build two reactors in northeastern China, using a new generation of technology known as a pebble-bed design. Critics and advocates describe it as safer than current reactors, though its cost-effectiveness unclear. The two reactors in Shidao, in Shandong Province in northeast China, were approved days before the Fukushima nuclear accident began with an earthquake and tsunami March 11. But the 50-month timetable for building them cannot start until the government lifts its hold on construction. “By the end of this year, maybe we’ll have some information from the government side Dr. Xu said. Nuclear power represented only 1.1 percent of China’s electricity generation capacity at the end of last year. With wind turbines and coal-fired power plants being installed at rates that far surpass those in any other country, nuclear power is on track to account for no more than 4 percent of electricity capacity by 2015. A big part of the appeal of nuclear energy for Chinese officials is that it supplies baseload power, meaning it is available 24 hours a day and seven days a week to meet needs. China passed the United States last year as the world’s largest installer of wind turbines, but wind still accounts for only 3.2 percent of China’s installed electricity generation capacity and less than 2 percent of electricity generated. Coal remains by far the dominant source of electricity in China, producing three-quarters of the country’s electricity. Nuclear power mainly displaces coal as a source of baseload power. That has also made it popular with Chinese officials, as they have set increasingly ambitious targets to slow the country’s rapid rise in emissions of global warming gases, in which the country already leads the world. Until reliable, large-scale storage of electricity is perfected for renewable energy sources like the wind and sun, “they’ve got to continue using nuclear as a fundamental part of their fuel mix, said James A. Maguire, the regional managing director for Asian infrastructure at Aon Risk Solutions, a risk management and insurance broker. Coal is the most polluting major source of electricity in terms of emissions of climate-changing gases, while nuclear power is one of the cleanest. Coal mining accidents also kill more than 2,000 people a year in China, and large areas of the countryside in northern China have been heavily polluted. China is paying particular attention to nuclear safety issues, however, because it has some of the world’s most densely populated rural areas. If a nuclear accident rendered even a small area around a power plant uninhabitable, many would need to be resettled. China now has an unusually varied fleet of nuclear reactors, using French, American, Russian and homegrown technology. While awarding contracts to a wide range of multinational nuclear power plant contractors, it has required that they provide documentation on exactly how to build the reactors. That will give China the ability to export reactors in a few years, in competition with industrialized nations, nuclear power industry experts warned. Demand outside China could revive if memories of the Fukushima accident fade or if worries about global warming become more pressing. China is not only acquiring technology. It is also creating economies of scale by building dozens of reactors at the same time. As a population equal to New York City’s moves into China’s cities each year demanding air-conditioners and other electricity-guzzling conveniences, consumption is likely to continue growing by double digits. “It’s the largest migration in history, so they need to build a lot of infrastructure, said Dennis Bracy, the chief executive of the U.S.-China Clean Energy Forum, a bilateral discussion group formed by the Chinese government with prominent Americans from previous Republican and Democratic administrations. “I believe they will stay with nuclear as part of the portfolio.", null, null, null));
        articles.add(new Article(null, null, "null Nuclear Power Gains in Status After Lobbying", "As the White House was putting together the energy plan that President Bush released last week, there had been almost no talk of nuclear power as a component of the nation's energy strategy. The nuclear industry thought this was a glaring omission, and a handful of top nuclear industry officials decided they needed to take their case to the administration. In mid-March, a cadre of seven nuclear power executives sought and won an hourlong meeting in the White House with Karl Rove, Mr. Bush's top political adviser. Also attending were Lawrence B. Lindsey, the president's top economic adviser, Andrew Lundquist, the executive director of Vice President Dick Cheney's energy task force, and others involved in devising the energy plan. ''We said, Look, we are an important player on this energy team and here are our vital statistics, and we think that you should start talking about nuclear when you talk about increasing the nation's supply,'' Christian H. Poindexter, chairman of the Constellation Energy Group, recalled today. And then a surprising thing happened. ''It was shortly after that, as a matter of fact I think the next night, when the vice president was being interviewed on television, he began to talk about nuclear power for the first time,'' Mr. Poindexter said. Mr. Cheney first discussed nuclear power as an alternative to dirtier fossil fuels in a March 21 interview on CNBC. ''If you want to do something about carbon dioxide emissions,'' he said, ''then you ought to build nuclear power plants because they don't emit any carbon dioxide, they don't emit greenhouse gases.'' Mr. Cheney had missed the meeting with nuclear executives because he was on Capitol Hill, talking to members of Congress who themselves were pushing nuclear energy. In a quick chain reaction, Mr. Cheney put the long-maligned nuclear power industry back on the political map. In the energy plan released last week, the administration breathed new life into the industry, declaring nuclear technology, which provides 20 percent of the nation's electricity, much safer than it was 20 years ago. Today, Mr. Cheney appeared before 350 nuclear industry executives meeting in Washington -- 100 more than showed up at last year's annual meeting of the Nuclear Energy Institute -- and told them the administration wanted to encourage the Nuclear Regulatory Commission to expedite applications for new reactors, relicense existing plants and ''increase the resources devoted to safety and enforcement as we prepare to increase nuclear generating capacity in the future.'' He said the administration also wanted to renew the Price-Anderson Act, which limits nuclear plant operators' liability in case of an accident. Mr. Poindexter is still incredulous. ''In my wildest dreams, when I was over at the White House in March, I couldn't imagine them getting so behind us,'' he said. He was skeptical for good reason. Few industries have enjoyed the kind of renaissance that nuclear power may be poised to undergo. Accidents at Three Mile Island in Pennsylvania and Chernobyl in Ukraine seemed to seal the industry's fate as too dangerous, too uncontrollable and too expensive to win back a frightened public or secure the financial backing of Wall Street. The last nuclear power plant to enter operation was ordered in 1973. There still is no solution to the vexing problem of nuclear waste storage. And while recent polling shows that Americans more lopsidedly oppose dirtier fossil fuels than they oppose nuclear power, they still do not want to live near nuclear power plants. For those wary of a nuclear revival, these problems are no less real today than they were two decades ago. ''The Bush administration should at most be looking to proceed with what the Nuclear Regulatory Commission was planning -- an orderly phase-out of existing power plants,'' said Paul L. Leventhal, president of the Nuclear Control Institute and co-director of the Senate investigation into the 1979 accident at Three Mile Island. ''Instead, they're talking about a new rebirth, and it frankly just doesn't make sense.'' The Union of Concerned Scientists, using data from the industry itself, says that aging plants have experienced eight forced shutdowns in the last 16 months. And Mr. Leventhal said that replacing coal with nuclear power would not appreciably diminish global warming because most of the pollutants that cause global warming come from cars and trucks. Another problem, and one that Mr. Cheney fully acknowledges, is the lack of a national repository for the storage of nuclear waste. In his speech today, the vice president warned that the lack of a storage site could be a deal killer. Without a site, he said, ''eventually the contribution we can count on from the nuclear industry will, in fact, decline.'' The storage problem will not be solved at Yucca Mountain in Nevada, if Nevada politicians and the gambling industry have anything to say about it. Senators Harry Reid, a Democrat, and John Ensign, a Republican, have made opposition to nuclear waste dumping in their state their priority. ''Until they get the waste problem solved,'' Mr. Reid said, ''nothing's going to happen on nuclear power.'' Peter Bradford, a former member of the Nuclear Regulatory Commission, who now teaches energy policy at Yale, said that apart from the safety issues, nuclear power was economically problematic. ''The types of long-term investment necessary to sustain nuclear energy are going to prove very hard to find in this kind of volatile marketplace,'' Mr. Bradford said. Still, there are cheerleaders. One is Representative Billy Tauzin, the Louisiana Republican who heads the Energy and Commerce Committee. He spoke today at the Nuclear Energy Institute's annual meeting and summed up the surprise that others feel at the recent turn of events. ''As we gather here in Washington,'' Mr. Tauzin said, ''who would have thunk that we'd be discussing the possibility of nuclear construction in this country?''", null, null, null));
        articles.add(new Article(null, null, "null The Nuclear Power Option", "In his sketchy speech on energy policy last week, President Bush placed a high priority on nuclear energy, which he described as ''one of the safest, cleanest sources of power in the world.'' The president had good reason to suggest an important role for this much-feared energy source. The price of natural gas, the current fuel of choice for power plants, has risen sharply. And there is mounting evidence that damage from global warming may dwarf any environmental risk posed by nuclear power. It is therefore critical to keep nuclear power as part of the nation's energy mix. But Mr. Bush will have to address some crucial concerns before the public will follow him down the nuclear path with much enthusiasm. For starters, there is the awkward fact that nuclear power plants pose a risk of proliferating the materials and skills to make nuclear weapons. That is not an issue in the United States, which already has a mammoth nuclear arsenal. But if the United States resurrects its stagnant nuclear industry, other nations may also turn to nuclear power, with the risk that rogue nations may someday use the fuel to make bombs. The Bush administration will need to find ways, perhaps through the nuclear nonproliferation review that started yesterday, to ensure that power plants do not become an easy route to nuclear weapons. Beyond that, Mr. Bush will need to ensure that the pools holding spent fuel at domestic nuclear plants can be made safe from terrorists. He will have to devise a backup plan for storing nuclear waste, should the proposed burial site at Yucca Mountain prove untenable after legal and regulatory setbacks. He will need to invest in new, potentially safer reactor designs to allay longstanding concerns about accidents. Finally, one familiar impediment to nuclear power -- the high capital costs required up front -- could remain troublesome, unless the cost of competing fuels soars higher. None of these concerns need rule out this promising source of power. But they will need to be addressed forthrightly. Editorial", null, null, null));
        articles.add(new Article(null, null, "Nuclear Power's New Day", "Technologies are born, grow, thrive and decline, much as living organisms do. That should not be surprising. Since they derive from human knowledge, their effective application must be learned, and they compete for social and economic territory. Nuclear power, a product of naval propulsion research, emerged in the United States in the 1950's. Its first use as a commercial energy source came about because it had obvious benefits for pollution control. A Pennsylvania utility, Duquesne Light, built the first commercial nuclear power reactor at Shippingport, Pa., in 1954. The utility had planned to build a coal-fired power plant. When the public objected to further smoke pollution around smoky Pittsburgh, Duquesne switched to nuclear power. Public acceptance of a new technology is essential to its growth. Nuclear power, associated in the public's mind with nuclear weapons, was probably commercialized prematurely, while its complexities were still being worked out. Its environmental benefits were not fully appreciated in the early decades because air pollution was abating under government regulation in the 1960's and 1970's, and global warming had not yet emerged as the ultimate environmental challenge. When conservation slowed electricity demand after the Arab oil embargo of 1973 and 1974, utilities canceled orders for new power plants, both nuclear and coal. Almost all new plants built since then have been fueled with natural gas. But the population of the United States is growing, adding the equivalent of one California every 10 years. Demand has caught up with supply even with significant improvements in energy efficiency and conservation, and the United States has become the world's leading greenhouse gas emitter. These factors make a renewal of nuclear power likely. The Nuclear Regulatory Commission has begun carefully extending licenses for existing reactors for an additional 20 years; eventually all 104 operating United States power reactors will probably be relicensed. Since they produce no air pollution or greenhouse gases, that's good news. The nuclear industry is consolidating, focusing experience and expertise. Several reactors that have been shut down will probably be restored to operation. Several others that were left unfinished when demand slowed will probably be completed. Two or three new advanced light-water reactors, designs the N.R.C. has already pre-licensed, will probably be built at sites that already have construction permits. One company, Exelon, is considering seeking licensing of a simpler reactor, designed so that it cannot melt down, that uses a bed of billiard-ball-sized ''pebbles'' of compacted uranium oxide and graphite as its fuel and helium as its coolant and working fluid, passing the fission-heated helium directly into a turbine to generate electricity even more cheaply than burning natural gas. If the pebble-bed modular reactor wins approval from the N.R.C., other utilities may decide to use this technology as well. Americans are beginning to understand one of the unique benefits of nuclear technology. A majority now say they approve of nuclear power, a shift that appears to indicate awareness that nuclear power does not produce greenhouse gases that lead to global warming. There is less evidence of public understanding of radiation and nuclear waste. Antinuclear activism began in the 1960's with concerns about the disposal of nuclear waste, and disposal continues to be the nuclear industry's Number 1 public-relations problem. The disposal debate is likely to move to center stage later this year, when the scientists and engineers evaluating Yucca Mountain, north of Las Vegas, as a possible permanent waste repository expect to deliver their final report. All energy technologies produce waste. Burning fossil fuels -- even relatively clean fuel like natural gas -- generates waste that cannot be contained within the power plant, as nuclear waste is, but must be released into the environment as air pollution and toxic waste. In the case of coal, burning releases ash that is mildly radioactive, because radioactive uranium and thorium are ubiquitous in the earth's crust, including coal seams. Even renewable technologies like wind power and solar photovoltaics produce waste: manufacturing the materials for the multitude of collectors necessary to gather up such diffuse sources as wind and sunlight requires burning fossil fuels. Thus wind or solar power systems release far more greenhouse gases across their life cycles than does a nuclear system of equivalent output. The great advantage of nuclear power is its ability to wrest enormous energy from a small volume of fuel. One metric ton of nuclear fuel produces energy equivalent to two million to three million tons of fossil fuel. Waste volumes are comparably scaled: fossil fuel systems generate hundreds of thousands of metric tons of gaseous, particulate and solid wastes, but nuclear systems produce less than 1,000 metric tons of high- and low-level waste per plant per year. The high-level waste is intensely radioactive at first, but its small volume means it can be and is effectively isolated and contained. When a nuclear plant is dismantled (few have been so far), the several hundred thousand tons of concrete, which is mildly radioactive, is buried in the same sort of commercial waste site used for radioactive medical and industrial wastes. Spent nuclear fuel loses radioactivity steadily; after 500 years it is no more radioactive than high-grade uranium ore. The risk of radioactive waste's seeping past multiple barriers would be small compared to health risks posed by air pollution from burning fossil fuels, which the World Health Organization estimates causes three million deaths a year, with 15,000 deaths annually in the United States from coal particulates alone. Substituting small, sequestered volumes of nuclear waste for vast, dispersed volumes of toxic wastes from fossil fuels could provide an enormous improvement in public health. The other risk that nuclear power supposedly raises is nuclear proliferation. In fact, no nation has developed nuclear weapons using plutonium from spent power reactor fuel. It's much easier to make weapons from plutonium bred specifically for that purpose. Inspection and proper accounting and control of nuclear materials are the answer to proliferation, not limits to nuclear power. France once burned coal; that nation's electricity is now 80 percent nuclear, with five times less air pollution and with carbon dioxide emissions 10 times lower than Germany's and 13 times lower than Denmark's. At a conference recently in Japan (another nuclear leader, with 36 percent nuclear electricity), French nuclear industry executive Anne Lanvergeon proposed improving the debate about nuclear power by creating an authoritative world database that would assess the advantages and disadvantages of each type of energy in terms of use of resources and economic, environmental and health impact. Measured against other energy sources, nuclear power would emerge at the top of such a list. Energy needs in the United States will grow in the coming decades, even with improved efficiency and more strenuous conservation. Nuclear energy needs to be a major component of our energy supply if we hope both to reduce air pollution and limit global warming. Richard Rhodes is the author of ''Nuclear Renewal'' and ''The Making of the Atomic Bomb.", null, null, null));
        obtained = toTest.mostPresent(articles, 34, banned);
        assertNotNull(obtained);
        assertEquals(toCheck.mostPresent(articles, 34, banned), obtained);

        obtained = toTest.mostPresent(articles, 789, banned);
        assertNotNull(obtained);
        assertEquals(toCheck.mostPresent(articles, 789, banned), obtained);

        //Tries without banned words
        assertNotEquals(obtained, toTest.mostPresent(articles, 789, null));

        expected.clear();
        expected.add(new OrderedEntryStringInt("a", 5));
        expected.add(new OrderedEntryStringInt("after", 5));
        expected.add(new OrderedEntryStringInt("an", 5));
        expected.add(new OrderedEntryStringInt("and", 5));
        expected.add(new OrderedEntryStringInt("as", 5));
        expected.add(new OrderedEntryStringInt("at", 5));
        expected.add(new OrderedEntryStringInt("be", 5));
        obtained = toTest.mostPresent(articles, 7, null);
        assertEquals(expected, obtained);

        banned.add("a");
        expected.clear();
        expected.add(new OrderedEntryStringInt("after", 5));
        expected.add(new OrderedEntryStringInt("an", 5));
        expected.add(new OrderedEntryStringInt("and", 5));
        expected.add(new OrderedEntryStringInt("as", 5));
        expected.add(new OrderedEntryStringInt("at", 5));
        expected.add(new OrderedEntryStringInt("be", 5));
        expected.add(new OrderedEntryStringInt("can", 5));
        obtained = toTest.mostPresent(articles, 7, banned);
        assertEquals(expected, obtained);

        banned.clear();
        banned.add("and");
        expected.clear();
        expected.add(new OrderedEntryStringInt("a", 5));
        expected.add(new OrderedEntryStringInt("after", 5));
        expected.add(new OrderedEntryStringInt("an", 5));
        expected.add(new OrderedEntryStringInt("as", 5));
        expected.add(new OrderedEntryStringInt("at", 5));
        expected.add(new OrderedEntryStringInt("be", 5));
        expected.add(new OrderedEntryStringInt("can", 5));
        obtained = toTest.mostPresent(articles, 7, banned);
        assertEquals(expected, obtained);

        banned.clear();
        banned.add("can");
        banned.add("be");
        expected.clear();
        expected.add(new OrderedEntryStringInt("a", 5));
        expected.add(new OrderedEntryStringInt("after", 5));
        expected.add(new OrderedEntryStringInt("an", 5));
        expected.add(new OrderedEntryStringInt("and", 5));
        expected.add(new OrderedEntryStringInt("as", 5));
        expected.add(new OrderedEntryStringInt("at", 5));
        expected.add(new OrderedEntryStringInt("could", 5));

        obtained = toTest.mostPresent(articles, 7, banned);
        assertEquals(expected, obtained);
    }

    /**
     * The only constructor of the class. It is declared as private to
     * prevent the default constructor to be created.
     */
    private MapSplitAnalyzerTest() {}
}

