package ru.practice.homeworks.data.mapper

import ru.practice.homeworks.data.remote.response.CatResponse
import ru.practice.homeworks.domain.model.CatDataModel
import javax.inject.Inject

class ApiResponceMapper @Inject constructor() {

    fun mapToCatDataModel(input: CatResponse) : CatDataModel {
        return CatDataModel(
            id = input.id,
            url = input.url,
        )
    }

    fun mapToCatDataModelList(input: List<CatResponse>) : List<CatDataModel> {
        return input.map { catResponse ->
            CatDataModel(
                id = catResponse.id,
                url = catResponse.url
            )
        }
    }

}