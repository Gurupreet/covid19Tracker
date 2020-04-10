package com.guru.covid19tracker.ui.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.guru.covid19tracker.R
import com.guru.covid19tracker.data.firebase.FirebaseManager
import com.guru.covid19tracker.data.firebase.FirebaseObserverType
import com.guru.covid19tracker.data.firebase.FirestoreResponseCompletionHandler
import com.guru.covid19tracker.models.Info
import com.guru.covid19tracker.utils.CardType
import kotlinx.android.synthetic.main.fragment_info.*

class InfoFragment : Fragment(), InfoListAdapter.OnListItemTapped{

    lateinit var infoListAdapter: InfoListAdapter
    lateinit var infoCardsAdapter: InfoCardsAdapter
    lateinit var symptomsCardsAdapter: InfoCardsAdapter

    val list = arrayListOf<Info>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInfo()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupCardsPrecautionData()
        setupCardsDataSymptoms()
        loadInfo()
    }

    private fun setupRecyclerView() {
        infoListAdapter = InfoListAdapter(this)
        recycler_view.layoutManager  = LinearLayoutManager(recycler_view.context)
        recycler_view.adapter = infoListAdapter

        infoCardsAdapter = InfoCardsAdapter(CardType.INFO)
        recycler_view_info.layoutManager = LinearLayoutManager(recycler_view_info.context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_info.adapter = infoCardsAdapter

        symptomsCardsAdapter = InfoCardsAdapter(CardType.SYMPTOMS)
        recycler_view_symptoms.layoutManager =  LinearLayoutManager(recycler_view_info.context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_symptoms.adapter = symptomsCardsAdapter


    }

    private fun loadInfo() {
        FirebaseManager.getInfoData(object : FirestoreResponseCompletionHandler {
            override fun onSuccess(result: Any?, observerType: FirebaseObserverType) {
                infoListAdapter?.apply {
                    list.clear()
                    list.addAll(result as ArrayList<Info>)
                    setData(list)
                }
            }
            override fun onFailure(result: Any?) {}
        })
    }

    private fun setupCardsPrecautionData() {
        val card1 = Info("1/6: Wash Hands", "Washing your hands with soap and water or using alcohol-based hand rub kills viruses that may be on your hands.")
        val card2 = Info("2/6: Social Distancing", "When someone coughs or sneezes they spray small liquid droplets from their nose or mouth which may contain virus. If you are too close, you can breathe in the droplets, including the COVID-19 virus if the person coughing has the disease.")
        val card3 = Info("3/6: Avoid touching eyes, nose and mouth", "Hands touch many surfaces and can pick up viruses. Once contaminated, hands can transfer the virus to your eyes, nose or mouth. From there, the virus can enter your body and can make you sick.")
        val card4 = Info("4/6: Respiratory hygiene", "This means covering your mouth and nose with your bent elbow or tissue when you cough or sneeze, nose or mouth. ")
        val card5 = Info("5/6: Stay home If sick", "Stay home if you feel unwell. If you have a fever, cough and difficulty breathing, seek medical attention and call in advance. Follow the directions of your local health authority.")
        val card6 = Info("6/6: Wear a mask If needed", "If it is essential for you to have someone bring you supplies or to go out, e.g. to buy food, then wear a mask to avoid infecting other people.")

        infoCardsAdapter.setData(arrayListOf(card1, card2, card3, card4, card5, card6))
    }

    private fun setupCardsDataSymptoms() {
        val card1 = Info("1/6: Fever, tiredness, and dry cough", "The main symptoms are Fever, Tiredness and dry cough. Please don't panic and contact helplines if you have any symptoms.")
        val card2 = Info("2/6: Sore Throat and Diarrhea", "Some cases have shown the symptoms of Sore Throat and Diarrhea.")
        val card3 = Info("3/6: Difficulty Breathing", "Around 1 out of every 6 people who gets COVID-19 becomes seriously ill and develops difficulty breathing. Older people, and those with underlying medical problems are more likely to develop serious illness")
        val card4 = Info("4/6: No Symptoms", "Some people become infected but don’t develop any symptoms and don't feel unwell. Most people (about 80%) recover from the disease without needing special treatment.")
        val card5 = Info("5/6: Incubation period", "The “incubation period” means the time between catching the virus and beginning to have symptoms of the disease. Most estimates of the incubation period for COVID-19 range from 1-14 days")
        val card6 = Info("6/6: What to Do", "In any case, if you have fever, cough and difficulty breathing seek medical care early to reduce the risk of developing a more severe infection. Antibiotics should not be used as a means of prevention or treatment of COVID-19.")

        symptomsCardsAdapter.setData(arrayListOf(card1, card2, card3, card4, card5, card6))
    }




    companion object {
        fun newInstance() = InfoFragment()
    }

    override fun onItemTapped(state: Info) {

    }
}
